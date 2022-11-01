package com.myapp.iam.controller;

import com.myapp.iam.dto.UserCreateRequestDto;
import com.myapp.iam.service.keycloak.KeycloakPasswordService;
import com.myapp.iam.service.keycloak.KeycloakUserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final KeycloakPasswordService passwordService;
    private final KeycloakUserManagementService userManagementService;

    @PostMapping
    private ResponseEntity<Void> createUser(@RequestBody UserCreateRequestDto request) {
        userManagementService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{userId}/password-reset")
    public ResponseEntity<Void> requestForgetPasswordEmail(@PathVariable("userId") String userId) {
        passwordService.sendResetPasswordEmail(userId);
        return ResponseEntity.noContent().build();
    }
}
