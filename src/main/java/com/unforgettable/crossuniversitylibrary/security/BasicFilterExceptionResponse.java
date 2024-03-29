package com.unforgettable.crossuniversitylibrary.security;

import com.unforgettable.crossuniversitylibrary.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BasicFilterExceptionResponse {
    public static void generateFilterExceptionResponse(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       HttpStatus httpStatus,
                                                       String exceptionMessage) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exceptionMessage,
                httpStatus,
                Timestamp.valueOf(LocalDateTime.now()),
                request.getRequestURI());

        new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValue(response.getOutputStream(), exceptionResponse);
    }
}
