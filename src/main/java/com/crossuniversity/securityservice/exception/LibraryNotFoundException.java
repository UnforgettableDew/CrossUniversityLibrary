package com.crossuniversity.securityservice.exception;

public class LibraryNotFoundException extends RuntimeException{
    public LibraryNotFoundException(String message) {
        super(message);
    }
}
