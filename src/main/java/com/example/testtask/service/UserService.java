package com.example.testtask.service;

import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.entity.User;

import java.util.List;

public interface UserService {

    UserResponse getUserById(Long id);

    List<UserResponse> searchUsers(
            String name,
            String email,
            String phone,
            String dateOfBirthAfter,
            int page,
            int size
    );


    User getById(Long id);

}
