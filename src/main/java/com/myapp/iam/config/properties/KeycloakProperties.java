package com.myapp.iam.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KeycloakProperties {
    /**
     * Realm user username with admin privileges.
     */
    @Value("${iam.keycloak.username:}")
    private String username;

    /**
     * Keycloak realm user password with admin privileges.
     */
    @Value("${iam.keycloak.password:}")
    private String password;

    /**
     * Keycloak created clientId per realm.
     */
    @Value("${keycloak.resource:}")
    private String clientId;

    /**
     * Keycloak created realm.
     */
    @Value("${keycloak.realm:}")
    private String realm;

    /**
     * Auth server URL connected to our Keycloak instance.
     */
    @Value("${keycloak.auth-server-url:}")
    private String authServerUrl;
}
