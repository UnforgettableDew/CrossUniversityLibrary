package com.unforgettable.crossuniversitylibrary.exception.bad_request;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
