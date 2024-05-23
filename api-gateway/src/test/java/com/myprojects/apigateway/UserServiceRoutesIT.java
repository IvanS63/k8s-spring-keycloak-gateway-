package com.myprojects.apigateway;

import static com.myprojects.apigateway.emulator.UserServiceEmulator.userSignUpRequest;
import static com.myprojects.apigateway.emulator.UserServiceEmulator.userSignupResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Integration tests for routes with id user-service
 */
public class UserServiceRoutesIT extends BaseIT {

    @Test
    public void shouldReturn200ForUserSignUpRequest() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "jonh@email.com";
        String password = "Qwerty_123";
        userService
                .when(userSignUpRequest())
                .respond(userSignupResponse(firstName, lastName, email));
        given().
                contentType(ContentType.JSON).
                body(request(firstName, lastName, email, password)).

        when().
                post("/user/users").

        then().
                statusCode(HttpStatus.CREATED.value()).
                body("firstName", equalTo(firstName)).
                body("lastName", equalTo(lastName)).
                body("email", equalTo(email)).
                body("creationDate", notNullValue());
        userService.verifyAllExpectedRequests();
    }

    private String request(String username, String firstname, String lastName, String password){
        return """
                {
                     "firstName": "%s",
                     "lastName": "%s",
                     "email": "%s",
                     "password" : "%s"
                 }
                """
                .formatted(firstname, lastName, username, password);
    }
}
