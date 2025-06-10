package com.example.testtask.service.impl;

import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.entity.User;
import com.example.testtask.exception.UserNotFoundException;
import com.example.testtask.mapper.UserMapper;
import com.example.testtask.repository.UserRepository;
import com.example.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
    @Cacheable(
            value = "userSearch",
            key   = """
                'searchUsers_' + #name + '_' + #email + '_' + #phone + '_' + \
                #dateOfBirthAfter + '_' + #pageable.pageNumber + '_' + #pageable.pageSize
                """
    )
    public List<UserResponse> searchUsers(String name,
                                          String email,
                                          String phone,
                                          String dateOfBirthAfter,
                                          Pageable pageable) {

        log.info("Поиск пользователей: name={}, email={}, phone={}, dateAfter={}",
                name, email, phone, dateOfBirthAfter);

        Specification<User> spec = (root, q, cb) -> cb.conjunction();

        if (name != null && !name.isBlank())
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%"));

        if (email != null && !email.isBlank())
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.join("emails").get("email"), email));

        if (phone != null && !phone.isBlank())
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.join("phones").get("phone"), phone));

        if (dateOfBirthAfter != null && !dateOfBirthAfter.isBlank()) {
            LocalDate date = LocalDate.parse(dateOfBirthAfter);
            spec = spec.and((root, q, cb) -> cb.greaterThan(root.get("dateOfBirth"), date));
        }

        spec = spec.and((root, q, cb) -> { q.distinct(true); return cb.conjunction(); });

        return userRepository.findAll(spec, pageable)
                .stream()
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
