package com.example.testtask.service;

public interface AuthService {
    String authenticateByEmail(String email, String password);
    String authenticateByPhone(String phone, String password);
}
