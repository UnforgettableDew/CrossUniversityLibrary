package com.unforgettable.crossuniversitylibrary.exception.bad_request;

public class UniversityAlreadyExistsException extends RuntimeException{
    public UniversityAlreadyExistsException(String message) {
        super(message);
    }
}
