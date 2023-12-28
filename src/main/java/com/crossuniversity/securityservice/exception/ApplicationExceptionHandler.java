package com.crossuniversity.securityservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(UserNotFoundException exception,
                                                                  HttpServletRequest request) {
        HttpStatus httpStatus = NOT_FOUND;
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUsernameAlreadyExistsException(UserAlreadyExistsException exception,
                                                                       HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);

    }
}
