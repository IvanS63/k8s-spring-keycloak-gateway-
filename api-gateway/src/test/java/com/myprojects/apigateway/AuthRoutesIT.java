package com.myprojects.apigateway;

import static com.myprojects.apigateway.emulator.KeycloakEmulator.USER_ACCESS_TOKEN;
import static com.myprojects.apigateway.emulator.KeycloakEmulator.USER_REFRESH_TOKEN;
import static com.myprojects.apigateway.emulator.KeycloakEmulator.userAccessTokenRequest;
import static com.myprojects.apigateway.emulator.KeycloakEmulator.userAccessTokenResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Integration test fpr route auth-tokens
 */
public class AuthRoutesIT extends BaseIT {

    @Test
    public void shouldReturn200ForAccessTokenRequestByUsernameAndPassword() {
        String username = "some-username";
        String password = "some-password";
        keycloakServer
                .when(userAccessTokenRequest(username, password))
                .respond(userAccessTokenResponse());
        given().
                contentType(ContentType.JSON).
                body(accessTokenRequest(username, password)).

        when().
                post("/auth/tokens").

        then().
                statusCode(HttpStatus.OK.value()).
                body("accessToken", equalTo(USER_ACCESS_TOKEN)).
                body("expiresIn", equalTo(300)).
                body("refreshToken", equalTo(USER_REFRESH_TOKEN)).
                body("refreshExpiresIn", equalTo(1800)).
                body("tokenType", equalTo("Bearer"));
        keycloakServer.verifyAllExpectedRequests();
    }

    @Test
    public void shouldReturn200ForAccessTokenRequestByRefreshToken() {
        keycloakServer
                .when(userAccessTokenRequest(USER_REFRESH_TOKEN))
                .respond(userAccessTokenResponse());
        given().
                contentType(ContentType.JSON).
                body(accessTokenRequest(USER_REFRESH_TOKEN)).

        when().
                post("/auth/tokens").

        then().
                statusCode(HttpStatus.OK.value()).
                body("accessToken", equalTo(USER_ACCESS_TOKEN)).
                body("expiresIn", equalTo(300)).
                body("refreshToken", equalTo(USER_REFRESH_TOKEN)).
                body("refreshExpiresIn", equalTo(1800)).
                body("tokenType", equalTo("Bearer"));
        keycloakServer.verifyAllExpectedRequests();
    }

    @Test
    @Disabled //TODO add exception handling
    public void shouldReturn400ForInvalidAccessTokenRequest() {
        given().
                contentType(ContentType.JSON).
                body(
                        """
                                {
                                    "username": "%s"
                                }
                                """
                ).

        when().
                post("/auth/tokens").

        then().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private String accessTokenRequest(String username, String password) {
        return """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """
                .formatted(username, password);
    }

    private String accessTokenRequest(String refreshToken) {
        return """
                {
                    "refreshToken": "%s"
                }
                """
                .formatted(refreshToken);
    }
}
