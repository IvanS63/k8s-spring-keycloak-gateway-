package com.myprojects.userservice.service.keycloak;

import com.myprojects.userservice.config.properties.KeycloakProperties;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakClientProvider {
    private Keycloak keycloakClient;
    private final KeycloakProperties properties;

    public Keycloak getKeycloakClient(){
        return Objects.nonNull(keycloakClient)
                ? keycloakClient
                : KeycloakBuilder.builder()
                        .serverUrl(properties.getServerUrl())
                        .realm(properties.getRealm())
                        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                        .clientId(properties.getClientId())
                        .clientSecret(properties.getClientSecret())
                        .build();
    }

    public RealmResource getRealmResource(){
        return getKeycloakClient().realm(properties.getRealm());
    }
}
