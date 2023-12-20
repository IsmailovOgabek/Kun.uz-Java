package com.example.exp;

public class AppForbiddenException extends RuntimeException {
    public AppForbiddenException(String message) {
        super(message);
    }
}
