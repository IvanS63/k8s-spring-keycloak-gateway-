package com.myprojects.userservice.exception;

import org.springframework.http.HttpStatus;

public abstract class GeneralException extends RuntimeException {

    public GeneralException(String detail) {
        super(detail);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getDetail() {
        return getMessage();
    }

    public abstract String getTitle();

    public abstract HttpStatus getStatus();
}
