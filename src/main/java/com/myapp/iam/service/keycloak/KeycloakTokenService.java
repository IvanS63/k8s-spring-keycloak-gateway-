package com.myapp.iam.service.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakTokenService {
    private final Keycloak keycloak;

    /**
     * Obtain admin keycloak access token (using admin username and password).
     *
     * @return Keycloak access token.
     */
    public AccessTokenResponse getAdminAccessToken() {
        return keycloak.tokenManager().getAccessToken();
    }
}
