package com.example.testtask.exception;

public class InvalidPhoneOrPasswordException extends RuntimeException {
    public InvalidPhoneOrPasswordException(String message) {
        super(message);
    }
}
