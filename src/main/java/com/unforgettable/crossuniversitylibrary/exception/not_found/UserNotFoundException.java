package com.unforgettable.crossuniversitylibrary.exception.not_found;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
