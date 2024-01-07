package com.crossuniversity.securityservice.exception.bad_request;

public class UniversityAlreadyExistsException extends RuntimeException{
    public UniversityAlreadyExistsException(String message) {
        super(message);
    }
}
