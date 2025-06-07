package com.example.testtask.repository;

import com.example.testtask.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    Optional<EmailData> findByEmail(String email);
}
