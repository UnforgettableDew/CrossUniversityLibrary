package com.unforgettable.crossuniversitylibrary.exception.bad_request;

public class LibraryBadRequestException extends RuntimeException{
    public LibraryBadRequestException(String message) {
        super(message);
    }
}
