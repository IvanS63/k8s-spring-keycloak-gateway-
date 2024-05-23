package com.myprojects.userservice.service;

import static java.lang.String.format;

import com.myprojects.userservice.entity.UserProfileEntity;
import com.myprojects.userservice.exception.EntityNotFoundException;
import com.myprojects.userservice.exception.UserAlreadyExistsException;
import com.myprojects.userservice.repository.UserProfileRepository;
import com.myprojects.userservice.service.keycloak.KeycloakService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final KeycloakService keycloakService;

    public UserProfileEntity createUser(UserProfileEntity user, String password) {
        if (userProfileRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new UserAlreadyExistsException(
                    format("User with email=%s already exists", user.getEmail()));
        }
        String keycloakId = keycloakService.createUser(user.getEmail());
        keycloakService.setUserPassword(keycloakId, password);
        return userProfileRepository.save(user.withKeycloakId(keycloakId));
    }

    public UserProfileEntity getUserProfile(String userId){
        return findUserProfileById(userId);
    }

    public void deleteUser(String userId) {
        UserProfileEntity userProfileEntity = findUserProfileById(userId);
        keycloakService.deleteUser(userProfileEntity.getKeycloakId());
        userProfileRepository.delete(userProfileEntity);
    }

    private UserProfileEntity findUserProfileById(String userId) {
        return userProfileRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User profile with id=%s not found", userId)));
    }
}
