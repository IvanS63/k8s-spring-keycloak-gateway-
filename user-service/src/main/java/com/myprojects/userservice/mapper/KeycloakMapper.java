package com.myprojects.userservice.mapper;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

import java.util.*;

@Mapper(componentModel = "spring")
public interface KeycloakMapper {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "enabled", constant = "true")
    UserRepresentation toUserRepresentation(String username, String email);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "type", constant = "password")
    @Mapping(target = "temporary", source = "temporary")
    @Mapping(target = "value", source = "newPassword")
    CredentialRepresentation toCredentialRepresentation(String newPassword, boolean temporary);

    @AfterMapping
    default void fillOtherFields(@MappingTarget UserRepresentation userRepresentation) {
        userRepresentation.setAttributes(Map.of("userId", Collections.singletonList(UUID.randomUUID().toString())));
    }
}
