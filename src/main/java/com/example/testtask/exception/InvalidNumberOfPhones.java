package com.example.testtask.exception;

public class InvalidNumberOfPhones extends RuntimeException {
    public InvalidNumberOfPhones(String message) {
        super(message);
    }
}
