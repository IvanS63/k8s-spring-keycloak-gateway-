package com.myprojects.userservice.service.keycloak;

import com.myprojects.userservice.dto.UserCreateRequestDto;
import com.myprojects.userservice.mapper.KeycloakMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserManagementService {
    private final KeycloakMapper mapper;
    private final RealmResource realmResource;

    public String createUser(UserCreateRequestDto request) {
        UserRepresentation userRepresentation = mapper.toUserRepresentation(request.getUsername(), request.getEmail());
        try (Response response = realmResource.users().create(userRepresentation)) {
            Response.Status status = response.getStatusInfo().toEnum();
            if (!status.equals(Response.Status.CREATED)) {
                //TODO add exception handling here
                throw new RuntimeException();
            }
            return userRepresentation.getId();
        }
    }

    public void deleteUser(String keycloakUserId) {
        try (Response response = realmResource.users().delete(keycloakUserId)) {
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                //TODO add exception handling here
                throw new RuntimeException();
            }
        }
    }
}
