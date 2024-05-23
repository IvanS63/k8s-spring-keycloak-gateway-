package com.myprojects.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends GeneralException {

    public UserAlreadyExistsException(String detail) {
        super(detail);
    }

    @Override
    public String getTitle() {
        return "User already exists";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
