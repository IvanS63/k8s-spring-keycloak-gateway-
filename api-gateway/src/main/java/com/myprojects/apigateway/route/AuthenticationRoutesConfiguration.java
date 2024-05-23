package com.myprojects.apigateway.route;

import static org.springframework.util.StringUtils.hasText;

import com.fasterxml.jackson.databind.JsonNode;
import com.myprojects.apigateway.config.AuthRoutesProperties;
import com.myprojects.apigateway.exception.AuthorizationServerException;
import com.myprojects.apigateway.exception.InvalidTokenRequestException;
import com.myprojects.apigateway.exception.UserAuthorizationException;
import com.myprojects.apigateway.util.JSONParser;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class AuthenticationRoutesConfiguration {
    public static final String AUTH_TOKENS_PATH = "/auth/tokens";

    private final AuthRoutesProperties properties;
    private final JSONParser parser;

    /**
     * Modifies paths, request and response bodies in order to route to the valid VVC portal endpoints.
     * <p>
     * Routes /auth/tokens to the Keycloak tokens endpoint. It returns 401 if the password isn't set
     * or Keycloak user is disabled. Otherwise, it generates Keycloak token.
     *
     * @return route locator implementation
     */
    @Bean
    public RouteLocator authRoutes(RouteLocatorBuilder builder) {
        return builder.routes().route("auth-tokens", route ->
                route
                        .path(AUTH_TOKENS_PATH)
                        .filters(filter -> filter
                                .setPath(MessageFormat.format("/realms/{0}/protocol/openid-connect/token", properties.getRealm()))
                                .modifyRequestBody(TokenGenerationRequest.class, String.class, MediaType.APPLICATION_FORM_URLENCODED.toString(), modifyRequestBodyFunction())
                                .modifyResponseBody(String.class, Object.class, modifyResponseBodyFunction()))
                        .uri(properties.getUrl())
        ).build();
    }

    private RewriteFunction<TokenGenerationRequest, String> modifyRequestBodyFunction() {
        return (exchange, tokenGenerationRequest) -> convertToUrlEncodedParameters(tokenGenerationRequest);
    }

    private RewriteFunction<String, Object> modifyResponseBodyFunction() {
        return (exchange, stringJSON) -> {
            JsonNode result = parser.parse(stringJSON);

            return switch (Objects.requireNonNull(exchange.getResponse().getStatusCode()).value()) {
                case 200 -> getTokenInfo(result);
                case 400, 401 -> Mono.error(new UserAuthorizationException());
                default -> Mono.error(new AuthorizationServerException());
            };
        };
    }

    private static Mono<Object> getTokenInfo(JsonNode result) {
        return Mono.just(TokenInformationResponse
                .builder()
                .accessToken(result.get("access_token").asText())
                .expiresIn(result.get("expires_in").asLong())
                .refreshToken(result.get("refresh_token").asText())
                .refreshExpiresIn(result.get("refresh_expires_in").asLong())
                .tokenType(result.get("token_type").asText())
                .build());
    }

    private Mono<String> convertToUrlEncodedParameters(TokenGenerationRequest request) {
        if (request != null) {
            if (request.containsUsernameAndPassword()) {
                return Mono.just(MessageFormat.format(
                        "grant_type={0}&client_id={1}&client_secret={2}&username={3}&password={4}",
                        "password",
                        properties.getClientId(), properties.getClientSecret(),
                        request.username, request.password));
            }
            if (request.containsRefreshToken()) {
                return Mono.just(MessageFormat.format(
                        "grant_type={0}&client_id={1}&refresh_token={2}",
                        "refresh_token",
                        properties.getClientId(), request.refreshToken));
            }
        }
        return Mono.error(new InvalidTokenRequestException());
    }

    private record TokenGenerationRequest(String username, String password, String refreshToken) {

        public boolean containsUsernameAndPassword() {
            return hasText(username) && hasText(password);
        }

        public boolean containsRefreshToken() {
            return hasText(refreshToken);
        }
    }

    @Builder(toBuilder = true)
    private record TokenInformationResponse(String accessToken, Long expiresIn, String refreshToken,
                                            Long refreshExpiresIn, String tokenType) {
    }
}
