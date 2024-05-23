package com.myprojects.apigateway.emulator;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_FORM_URLENCODED;

import java.text.MessageFormat;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpHeaders;

public class KeycloakEmulator {
    private static final String REALM = "SomeTestRealm";
    private static final String KEYCLOAK_CLIENT_ID = "some-client-id";
    private static final String KEYCLOAK_CLIENT_SECRET = "some-client-secret";
    public static final String USER_ACCESS_TOKEN = "some-access-token";
    public static final String USER_REFRESH_TOKEN = "some-refresh-token";

    public static HttpRequest userAccessTokenRequest(String username, String password) {
        return request()
                .withMethod("POST")
                .withPath(MessageFormat.format("/realms/{0}/protocol/openid-connect/token", REALM))
                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED + ".*")
                .withBody(MessageFormat.format(
                        "grant_type={0}&client_id={1}&client_secret={2}&username={3}&password={4}",
                        "password",
                        KEYCLOAK_CLIENT_ID, KEYCLOAK_CLIENT_SECRET,
                        username, password));
    }

    public static HttpRequest userAccessTokenRequest(String refreshToken) {
        return request()
                .withMethod("POST")
                .withPath(MessageFormat.format("/realms/{0}/protocol/openid-connect/token", REALM))
                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED + ".*")
                .withBody(MessageFormat.format(
                        "grant_type={0}&client_id={1}&refresh_token={2}",
                        "refresh_token",
                        KEYCLOAK_CLIENT_ID, refreshToken));
    }

    public static HttpResponse userAccessTokenResponse() {
        return response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                        """
                                {
                                     "access_token": "%s",
                                     "expires_in": 300,
                                     "refresh_token": "%s",
                                     "refresh_expires_in": 1800,
                                     "token_type": "Bearer"
                                 }
                                """.formatted(USER_ACCESS_TOKEN, USER_REFRESH_TOKEN)
                );
    }
}
