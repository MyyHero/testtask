package com.example.testtask.service;

import com.example.testtask.dto.request.UserUpdateRequest;
import com.example.testtask.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserResponse> getUserById(Long id);

    List<UserResponse> searchUsers(
            String name,
            String email,
            String phone,
            String dateOfBirthAfter,
            int page,
            int size
    );

    void updateUser(Long userId, UserUpdateRequest request);

    boolean isEmailTaken(String email);

    boolean isPhoneTaken(String phone);
}
