package com.crossuniversity.securityservice.exception;

import com.crossuniversity.securityservice.exception.bad_request.*;
import com.crossuniversity.securityservice.exception.forbidden.LibraryAccessException;
import com.crossuniversity.securityservice.exception.forbidden.DocumentAccessException;
import com.crossuniversity.securityservice.exception.not_found.DocumentNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.LibraryNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.UniversityNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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
        ExceptionResponse response = getExceptionResponse(exception.getMessage(), request.getRequestURI(), httpStatus);

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            UserAlreadyExistsException.class,
            UniversityAlreadyExistsException.class,
            LibraryBadRequestException.class,
            DocumentBadRequestException.class,
            AuthenticationException.class,
            NotMatchException.class,
            OutOfSpaceException.class,
            IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(Exception exception,
                                                                       HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;
        ExceptionResponse response = getExceptionResponse(exception.getMessage(), request.getRequestURI(), httpStatus);

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            LibraryAccessException.class,
            DocumentAccessException.class})
    public ResponseEntity<ExceptionResponse> handleAccessException(Exception exception,
                                                                   HttpServletRequest request) {
        HttpStatus httpStatus = FORBIDDEN;
        ExceptionResponse response = getExceptionResponse(exception.getMessage(), request.getRequestURI(), httpStatus);

        return new ResponseEntity<>(response, httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception,
                                                                     HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;
        List<String> violationsList = exception.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).toList();

        ExceptionResponse response = getExceptionResponse(violationsList.toString(), request.getRequestURI(), httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    //TODO
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                        HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;

        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        ExceptionResponse response = getExceptionResponse(errors.toString(), request.getRequestURI(), httpStatus);

        return new ResponseEntity<>(response, httpStatus);
    }

    private ExceptionResponse getExceptionResponse(String exceptionMessage, String requestURI, HttpStatus httpStatus) {
        log.error(exceptionMessage);

        return ExceptionResponse.builder()
                .message(exceptionMessage)
                .httpStatus(httpStatus)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(requestURI)
                .build();
    }
}
