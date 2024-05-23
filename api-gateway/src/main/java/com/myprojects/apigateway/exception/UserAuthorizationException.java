package com.myprojects.apigateway.exception;

import org.springframework.http.HttpStatus;

public class UserAuthorizationException extends GeneralException{

    @Override
    public String getTitle() {
        return "User not authorized";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
