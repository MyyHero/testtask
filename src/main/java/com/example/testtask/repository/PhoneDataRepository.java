package com.example.testtask.repository;

import com.example.testtask.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    Optional<PhoneData> findByPhone(String phone);
}
