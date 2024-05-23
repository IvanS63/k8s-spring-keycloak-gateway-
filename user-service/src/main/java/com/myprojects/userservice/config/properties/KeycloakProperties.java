package com.myprojects.userservice.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("keycloak")
public class KeycloakProperties {
    /**
     * Created clientId in Keycloak realm.
     */
    private final String clientId;

    /**
     * Created clientSecret in Keycloak realm.
     */
    private final String clientSecret;

    /**
     * Keycloak created realm.
     */
    private final String realm;

    /**
     * Auth server URL connected to our Keycloak instance.
     */
    private final String serverUrl;
}
