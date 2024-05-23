package com.myprojects.apigateway.emulator;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

public class UserServiceEmulator {

    public static HttpRequest userSignUpRequest() {
        return request()
                .withMethod("POST")
                .withPath("/users");
    }

    public static HttpResponse userSignupResponse(String firstname, String lastName, String email) {
        return response()
                .withStatusCode(201)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                        """
                                {
                                      "firstName": "%s",
                                      "lastName": "%s",
                                      "email": "%s",
                                      "creationDate": "2024-05-23T21:38:20.8641732"
                                  }
                                """.formatted(firstname, lastName, email)
                );
    }
}
