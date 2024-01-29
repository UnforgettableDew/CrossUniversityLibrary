package com.unforgettable.crossuniversitylibrary.exception.not_found;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
