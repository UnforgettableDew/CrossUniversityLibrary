package com.crossuniversity.securityservice.exception;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
