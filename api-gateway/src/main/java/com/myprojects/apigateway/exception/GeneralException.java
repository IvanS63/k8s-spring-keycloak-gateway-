package com.myprojects.apigateway.exception;

import org.springframework.http.HttpStatus;

public abstract class GeneralException extends RuntimeException {

    public GeneralException(String detail) {
        super(detail);
    }

    public GeneralException() {
        super();
    }

    public String getDetail() {
        return getMessage();
    }

    public abstract String getTitle();

    public abstract HttpStatus getStatus();
}
