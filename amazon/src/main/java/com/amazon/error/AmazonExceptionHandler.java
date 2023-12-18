package com.amazon.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AmazonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({WebClientResponseException.class, AmazonException.class})
    protected ResponseEntity<Object> handleException(RuntimeException ex) {
        return new ResponseEntity<>(new AmazonError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
