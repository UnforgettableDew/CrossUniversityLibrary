package com.crossuniversity.securityservice.exception.forbidden;

public class LibraryAccessException extends RuntimeException{
    public LibraryAccessException(String message) {
        super(message);
    }
}
