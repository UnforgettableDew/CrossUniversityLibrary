package com.crossuniversity.securityservice.exception.not_found;

public class UniversityNotFoundException extends RuntimeException{
    public UniversityNotFoundException(String message) {
        super(message);
    }
}
