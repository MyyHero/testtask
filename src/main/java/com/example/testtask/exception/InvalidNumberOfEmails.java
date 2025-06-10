package com.example.testtask.exception;

public class InvalidNumberOfEmails extends RuntimeException {
    public InvalidNumberOfEmails(String message) {
        super(message);
    }
}
