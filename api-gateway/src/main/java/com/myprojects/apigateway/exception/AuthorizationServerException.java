package com.myprojects.apigateway.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationServerException extends GeneralException{

    @Override
    public String getTitle() {
        return "Error while accessing Auth server";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
