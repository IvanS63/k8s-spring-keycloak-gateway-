package com.myprojects.userservice.controller;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.myprojects.userservice.BaseIT;
import com.myprojects.userservice.entity.UserProfileEntity;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Integration tests for {@link UserProfileController#getUserProfile(String, String)}.
 */
public class GetUserProfileEndpointIT extends BaseIT {

    @Test
    public void shouldReturn200() {
        UserProfileEntity entity = userProfileRepository.save(
                UserProfileEntity.builder()
                        .keycloakId(UUID.randomUUID().toString())
                        .firstName("John")
                        .lastName("Doe")
                        .email("johndoe@email.com").build());
        given().
                contentType(ContentType.JSON).
                auth().oauth2(tokenUtil.generateUserToken(entity.getKeycloakId())).

        when().
                get("/users/" + entity.getId()).

        then().
                statusCode(HttpStatus.OK.value()).
                body("id", equalTo(entity.getId().toString())).
                body("firstName", equalTo(entity.getFirstName())).
                body("lastName", equalTo(entity.getLastName())).
                body("email", equalTo(entity.getEmail())).
                body("creationDate", notNullValue());
    }

    @Test
    public void shouldReturn404IfUserWasNotFound() {
        String userId = UUID.randomUUID().toString();
        given().
                contentType(ContentType.JSON).
                auth().oauth2(tokenUtil.generateUserToken(userId)).

        when().
                get("/users/" + userId).

        then().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body("title", equalTo(format("User profile with id=%s not found", userId)));
    }

    @Test
    public void shouldReturn401IfAccessTokenWasNotProvidedInTheRequest() {
        String userId = UUID.randomUUID().toString();
        given().
                contentType(ContentType.JSON).

        when().
                get("/users/" + userId).

        then().
                statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
