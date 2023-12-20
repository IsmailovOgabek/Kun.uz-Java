package com.example.exp;

public class ItemAlreadyExistsException  extends RuntimeException{
    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
