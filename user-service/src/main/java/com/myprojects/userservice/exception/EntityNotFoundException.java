package com.myprojects.userservice.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends GeneralException {

    public EntityNotFoundException(String detail) {
        super(detail);
    }

    @Override
    public String getTitle() {
        return getDetail();
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
