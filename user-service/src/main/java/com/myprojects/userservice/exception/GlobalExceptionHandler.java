package com.myprojects.userservice.exception;

import com.myprojects.userservice.model.ApiExceptionDto;
import com.myprojects.userservice.model.InputViolationDto;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiExceptionDto> generalExceptionHandler(
            GeneralException ex) {

        return new ResponseEntity<>(
                ApiExceptionDto.builder()
                        .title(ex.getTitle())
                        .detail(ex.getDetail()).build(),
                ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> emptyParamsValidationErrorHandler(
            MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                ApiExceptionDto.builder()
                        .title("Field validation errors")
                        .inputViolations(Stream.concat(getFieldViolationErrors(ex).stream(),
                                getObjectViolationErrors(ex).stream()).toList())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    private List<InputViolationDto> getFieldViolationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> InputViolationDto.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
    }

    private List<InputViolationDto> getObjectViolationErrors(BindingResult bindingResult) {
        return bindingResult.getGlobalErrors()
                .stream()
                .map(error -> InputViolationDto.builder()
                        .field(error.getObjectName())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
    }
}
