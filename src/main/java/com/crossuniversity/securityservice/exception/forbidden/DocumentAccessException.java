package com.crossuniversity.securityservice.exception.forbidden;

public class DocumentAccessException extends RuntimeException{
    public DocumentAccessException(String message) {
        super(message);
    }
}
