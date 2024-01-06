package com.crossuniversity.securityservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            UserNotFoundException.class,
            DocumentNotFoundException.class,
            LibraryNotFoundException.class,
            UniversityNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(Exception exception,
                                                                             HttpServletRequest request) {
        HttpStatus httpStatus = NOT_FOUND;
        log.error(exception.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            UserAlreadyExistsException.class,
            AuthenticationException.class,
            OutOfSpaceException.class})
    public ResponseEntity<ExceptionResponse> handleUsernameAlreadyExistsException(Exception exception,
                                                                                  HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;
        log.error(exception.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {AccessException.class})
    public ResponseEntity<ExceptionResponse> handleAccessException(AccessException exception,
                                                                   HttpServletRequest request) {
        HttpStatus httpStatus = FORBIDDEN;
        log.error(AccessException.class.getName() + ": " + exception.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }
}
