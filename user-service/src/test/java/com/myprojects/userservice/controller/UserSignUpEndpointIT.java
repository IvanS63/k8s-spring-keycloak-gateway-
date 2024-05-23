package com.myprojects.userservice.controller;

import static com.myprojects.userservice.emulator.KeycloakEmulator.clientTokenCreationRequest;
import static com.myprojects.userservice.emulator.KeycloakEmulator.createUserRequest;
import static com.myprojects.userservice.emulator.KeycloakEmulator.createUserResponse;
import static com.myprojects.userservice.emulator.KeycloakEmulator.setPasswordRequest;
import static com.myprojects.userservice.emulator.KeycloakEmulator.tokenCreationResponse;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.myprojects.userservice.BaseIT;
import com.myprojects.userservice.entity.UserProfileEntity;
import com.myprojects.userservice.model.UserSignUpRequestDto;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpResponse;
import org.springframework.http.HttpStatus;

/**
 * Integration tests for {@link UserProfileController#userSignUp(UserSignUpRequestDto)} endpoint.
 */
public class UserSignUpEndpointIT extends BaseIT {

    @Test
    public void shouldReturn201ForUserSignUp() {
        UserSignUpRequestDto request = UserSignUpRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .password("Qwerty_12345")
                .build();
        keycloakServer
                .when(clientTokenCreationRequest())
                .respond(tokenCreationResponse());
        keycloakServer
                .when(createUserRequest(request.getFirstName() + " " + request.getLastName(), request.getEmail()))
                .respond(createUserResponse());
        keycloakServer
                .when(setPasswordRequest(request.getPassword()))
                .respond(HttpResponse.response().withStatusCode(HttpStatus.OK.value()));
        given().
                contentType(ContentType.JSON).
                body(request).

        when().
                post("/users").

        then().
                statusCode(HttpStatus.CREATED.value()).
                body("firstName", equalTo(request.getFirstName())).
                body("lastName", equalTo(request.getLastName())).
                body("email", equalTo(request.getEmail())).
                body("creationDate", notNullValue());
        keycloakServer.verifyAllExpectedRequests();
        assertTrue(userProfileRepository.existsByEmailIgnoreCase(request.getEmail()));
    }

    @Test
    public void shouldReturn409IfUserWithSuchEmailAlreadyExistsInDB() {
        userProfileRepository.save(
                UserProfileEntity.builder()
                    .keycloakId(UUID.randomUUID().toString())
                    .firstName("John")
                    .lastName("Doe")
                    .email("johndoe@email.com").build());
        UserSignUpRequestDto request = UserSignUpRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .password("Qwerty_12345")
                .build();
        keycloakServer
                .when(clientTokenCreationRequest())
                .respond(tokenCreationResponse());
        keycloakServer
                .when(createUserRequest(request.getFirstName() + " " + request.getLastName(), request.getEmail()))
                .respond(createUserResponse());
        given().
                contentType(ContentType.JSON).
                body(request).

        when().
                post("/users").

        then().
                statusCode(HttpStatus.CONFLICT.value()).
                body("title", equalTo("User already exists")).
                body("detail", equalTo(format("User with email=%s already exists", "johndoe@email.com")));
        keycloakServer.verifyAllExpectedRequests();
    }

    @Test
    public void shouldReturn409IfUserWithSuchEmailAlreadyExistsInKeycloak() {
        UserSignUpRequestDto request = UserSignUpRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .password("Qwerty_12345")
                .build();
        keycloakServer
                .when(clientTokenCreationRequest())
                .respond(tokenCreationResponse());
        keycloakServer
                .when(createUserRequest(request.getFirstName() + " " + request.getLastName(), request.getEmail()))
                .respond(HttpResponse.response().withStatusCode(409));
        given().
                contentType(ContentType.JSON).
                body(request).

        when().
                post("/users").

        then().
                statusCode(HttpStatus.CONFLICT.value()).
                body("title", equalTo("User already exists")).
                body("detail", equalTo(format("User with email %s already exists in Keycloak", "johndoe@email.com")));
        keycloakServer.verifyAllExpectedRequests();
        assertFalse(userProfileRepository.existsByEmailIgnoreCase(request.getEmail()));
    }

    @Test
    public void shouldReturn400IfUserRequestParamFailInputViolations() {
        UserSignUpRequestDto request = UserSignUpRequestDto.builder()
                .firstName(RandomStringUtils.randomAlphabetic(130))
                .lastName(RandomStringUtils.randomAlphabetic(130))
                .email(RandomStringUtils.randomAlphabetic(130))
                .password("1111111")
                .build();
        given().
                contentType(ContentType.JSON).
                body(request).

        when().
                post("/users").

        then().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body("title", equalTo("Field validation errors")).
                body("inputViolations", hasSize(4));
        assertFalse(userProfileRepository.existsByEmailIgnoreCase(request.getEmail()));
    }
}
