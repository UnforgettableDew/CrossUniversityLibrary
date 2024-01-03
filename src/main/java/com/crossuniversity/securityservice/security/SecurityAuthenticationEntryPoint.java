package com.crossuniversity.securityservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        BasicFilterExceptionResponse.generateFilterExceptionResponse(
                request,
                response,
                UNAUTHORIZED,
                "User unauthorized"
        );
    }
}
