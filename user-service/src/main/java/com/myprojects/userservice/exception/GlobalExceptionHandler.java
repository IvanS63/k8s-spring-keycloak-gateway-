package com.myprojects.userservice.exception;

import com.myprojects.userservice.model.ApiExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiExceptionDto> userNotFoundExceptionHandler(
            UserAlreadyExistsException ex) {

        return new ResponseEntity<>(
                ApiExceptionDto.builder().title(ex.getTitle()).detail(ex.getDetail()).build(),
                ex.getStatus());
    }
}
