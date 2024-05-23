package com.myprojects.userservice.service.keycloak;

import static java.lang.String.format;

import com.myprojects.userservice.exception.UserAlreadyExistsException;
import com.myprojects.userservice.mapper.KeycloakMapper;
import javax.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

    private final KeycloakClientProvider keycloakClientProvider;
    private final KeycloakMapper mapper;

    public String createUser(String username, String email) {
        UserRepresentation userRepresentation = mapper.toUserRepresentation(username, email);
        try (Response response = keycloakClientProvider.getRealmResource().users().create(userRepresentation)) {
            Response.Status status = response.getStatusInfo().toEnum();
            if (status.equals(Status.CONFLICT)){
                throw new UserAlreadyExistsException(format("User with email %s already exists in Keycloak", email));
            }
            if (!status.equals(Response.Status.CREATED)) {
                throw new RuntimeException();
            }
            return userRepresentation.getAttributes().get("userId").stream().findFirst().get();
        }
    }

    public void deleteUser(String keycloakUserId) {
        try (Response response = keycloakClientProvider.getRealmResource().users().delete(keycloakUserId)) {
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                //TODO add exception handling here
                throw new RuntimeException();
            }
        }
    }
}
