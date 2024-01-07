package com.crossuniversity.securityservice.exception;

import com.crossuniversity.securityservice.exception.bad_request.*;
import com.crossuniversity.securityservice.exception.forbidden.LibraryAccessException;
import com.crossuniversity.securityservice.exception.forbidden.DocumentAccessException;
import com.crossuniversity.securityservice.exception.not_found.DocumentNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.LibraryNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.UniversityNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ExceptionResponse> handleNotFoundException(Exception exception,
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
            UniversityAlreadyExistsException.class,
            LibraryBadRequestException.class,
            DocumentBadRequestException.class,
            AuthenticationException.class,
            NotMatchException.class,
            OutOfSpaceException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(Exception exception,
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

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            LibraryAccessException.class,
            DocumentAccessException.class})
    public ResponseEntity<ExceptionResponse> handleAccessException(Exception exception,
                                                                   HttpServletRequest request) {
        HttpStatus httpStatus = FORBIDDEN;
        log.error(exception.getMessage());

        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }
}
