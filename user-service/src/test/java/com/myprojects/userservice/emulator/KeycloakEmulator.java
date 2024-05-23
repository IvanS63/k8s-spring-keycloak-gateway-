package com.myprojects.userservice.emulator;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpHeaders;

public class KeycloakEmulator {

    private static final String realm = "SomeTestRealm";
    private static final String CLIENT_TOKEN = "some_client_keycloak_token";
    private static final String CLIENT_ID = "user-service-test-client-id";
    private static final String CLIENT_SECRET = "user-service-test-client-secret";
    private static final String KEYCLOAK_USER_ID = UUID.randomUUID().toString();

    public static HttpRequest clientTokenCreationRequest() {
        return request()
                .withMethod("POST")
                .withPath(MessageFormat.format("/realms/{0}/protocol/openid-connect/token", realm))
                .withHeader("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()))
                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED + ".*")
                .withBody("grant_type=client_credentials");
    }

    public static HttpResponse tokenCreationResponse() {
        return response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                        """
                                {
                                    "access_token": "%s",
                                    "expires_in": 60,
                                    "refresh_expires_in": 1800,
                                    "token_type": "Bearer",
                                    "not-before-policy": 0,
                                    "session_state": "6900f902-e324-471c-aff9-15a9a10bebbd",
                                    "scope": "email profile"
                                }
                                """.formatted(CLIENT_TOKEN)
                );
    }

    public static HttpRequest createUserRequest(String username, String email) {
        return request()
                .withHeader("Authorization", "Bearer some_client_keycloak_token")
                .withMethod("POST")
                .withPath(MessageFormat.format("/admin/realms/{0}/users", realm))
                .withBody(json(
                        format("{\"username\": \"%s\",\"email\": \"%s\"}", username, email),
                        MatchType.ONLY_MATCHING_FIELDS));
    }

    public static HttpResponse createUserResponse() {
        return response()
                .withStatusCode(201)
                .withContentType(MediaType.APPLICATION_JSON)
                .withHeader("Location",
                        MessageFormat.format("http://somehost:8080/admin/realms/{0}/users/{1}",
                                realm,
                                KEYCLOAK_USER_ID));
    }

    public static HttpRequest setPasswordRequest(String password){
        return request()
                .withHeader("Authorization", "Bearer some_client_keycloak_token")
                .withMethod("PUT")
                .withPath(MessageFormat.format("/admin/realms/{0}/users/{1}/reset-password", realm, KEYCLOAK_USER_ID))
                .withBody(json(
                        format("{\"type\": \"password\",\"value\": \"%s\",\"temporary\":false}", password),
                        MatchType.ONLY_MATCHING_FIELDS));
    }
}
