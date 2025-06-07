package com.example.testtask.service.impl;

import com.example.testtask.dto.request.UserUpdateRequest;
import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.entity.*;
import com.example.testtask.exception.EmailAlreadyExistsException;
import com.example.testtask.exception.PhoneNumberAlreadyExistsException;
import com.example.testtask.exception.UserNotFoundException;
import com.example.testtask.mapper.UserMapper;
import com.example.testtask.repository.*;
import com.example.testtask.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "users", key = "#id")
    public Optional<UserResponse> getUserById(Long id) {
        log.info("Получение пользователя по ID: {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
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

    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "userSearch", allEntries = true),
            @CacheEvict(value = "emailCheck", allEntries = true),
            @CacheEvict(value = "phoneCheck", allEntries = true)
    })
    @Transactional
    @Override
    public void updateUser(Long userId, UserUpdateRequest request) {
        log.info("Обновление пользователя ID={} с новыми данными: {}", userId, request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID={} не найден", userId);
                    return new UserNotFoundException("User not found");
                });

        for (String email : request.getEmails()) {
            emailDataRepository.findByEmail(email)
                    .filter(existing -> !existing.getUser().getId().equals(userId))
                    .ifPresent(e -> {
                        log.warn("Email '{}' уже занят другим пользователем", email);
                        throw new EmailAlreadyExistsException("Email already taken: " + email);
                    });
        }

        for (String phone : request.getPhones()) {
            phoneDataRepository.findByPhone(phone)
                    .filter(existing -> !existing.getUser().getId().equals(userId))
                    .ifPresent(p -> {
                        log.warn("Телефон '{}' уже занят другим пользователем", phone);
                        throw new PhoneNumberAlreadyExistsException("Phone already taken: " + phone);
                    });
        }

        user.getEmails().clear();
        user.getPhones().clear();

        request.getEmails().forEach(e -> user.getEmails().add(
                EmailData.builder().user(user).email(e).build()));

        request.getPhones().forEach(p -> user.getPhones().add(
                PhoneData.builder().user(user).phone(p).build()));

        userMapper.updateUserFromDto(request, user);
        userRepository.save(user);

        log.info("Пользователь ID={} успешно обновлён", userId);
    }

    @Cacheable(value = "emailCheck", key = "#email")
    @Override
    public boolean isEmailTaken(String email) {
        return emailDataRepository.findByEmail(email).isPresent();
    }

    @Cacheable(value = "phoneCheck", key = "#phone")
    @Override
    public boolean isPhoneTaken(String phone) {
        return phoneDataRepository.findByPhone(phone).isPresent();
    }
}
