package com.myprojects.apigateway.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenRequestException extends GeneralException {

    @Override
    public String getTitle() {
        return "Token request should either contain username and password or refresh token";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
