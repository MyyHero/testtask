package com.example.testtask.service;

import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponse getUserById(Long id);

    List<UserResponse> searchUsers(
            String name,
            String email,
            String phone,
            String dateOfBirthAfter,
            Pageable pageable
    );


    User getById(Long id);

}
