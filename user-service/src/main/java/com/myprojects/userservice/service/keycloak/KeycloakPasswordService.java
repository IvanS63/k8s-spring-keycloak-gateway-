package com.myprojects.userservice.service.keycloak;

import com.myprojects.userservice.mapper.KeycloakMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakPasswordService {
    private final RealmResource realmResource;
    private final KeycloakMapper keycloakMapper;

    public void requestResetPassword(String keycloakUserId, String newPasswordValue) {
        CredentialRepresentation credentialRepresentation = keycloakMapper.toCredentialRepresentation(newPasswordValue, false);
        realmResource.users().get(keycloakUserId).resetPassword(credentialRepresentation);
    }

    public void sendResetPasswordEmail(String keycloakUserId){
        realmResource.users()
                .get(keycloakUserId)
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }


}
