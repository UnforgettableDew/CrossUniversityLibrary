package com.crossuniversity.securityservice.exception.bad_request;

public class LibraryBadRequestException extends RuntimeException{
    public LibraryBadRequestException(String message) {
        super(message);
    }
}
