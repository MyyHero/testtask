package com.example.testtask.service.impl;

import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.entity.*;
import com.example.testtask.exception.UserNotFoundException;
import com.example.testtask.mapper.UserMapper;
import com.example.testtask.repository.UserRepository;
import com.example.testtask.service.EmailService;
import com.example.testtask.service.PhoneService;
import com.example.testtask.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        log.info("Получение пользователя по ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID={} не найден", id);
                    return new UserNotFoundException("Пользователь не найден");
                });
    }


    @Override
    @Cacheable(value = "userSearch", key = "#root.methodName + '_' + #name + '_' + #email + '_' + #phone + '_' + #dateOfBirthAfter + '_' + #page + '_' + #size")
    public List<UserResponse> searchUsers(String name, String email, String phone, String dateOfBirthAfter, int page, int size) {
        log.info("Поиск пользователей по фильтрам: name={}, email={}, phone={}, dateOfBirthAfter={}", name, email, phone, dateOfBirthAfter);

        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%"));
            }
            if (email != null) {
                predicates.add(cb.equal(root.join("emails").get("email"), email));
            }
            if (phone != null) {
                predicates.add(cb.equal(root.join("phones").get("phone"), phone));
            }
            if (dateOfBirthAfter != null) {
                predicates.add(cb.greaterThan(root.get("dateOfBirth"), LocalDate.parse(dateOfBirthAfter)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, PageRequest.of(page, size)).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("Пользователь с ID={} не найден", id);
            return new UserNotFoundException("Пользователь не найден");
        });
    }


}
