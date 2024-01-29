package com.unforgettable.crossuniversitylibrary.exception.not_found;

public class LibraryNotFoundException extends RuntimeException{
    public LibraryNotFoundException(String message) {
        super(message);
    }
}
