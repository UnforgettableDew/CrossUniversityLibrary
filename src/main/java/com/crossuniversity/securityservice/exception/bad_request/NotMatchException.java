package com.crossuniversity.securityservice.exception.bad_request;

public class NotMatchException extends RuntimeException{
    public NotMatchException(String message) {
        super(message);
    }
}
