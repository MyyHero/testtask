package com.example.testtask.service.impl;

import com.example.testtask.entity.User;
import com.example.testtask.exception.InvalidEmailOrPasswordException;
import com.example.testtask.exception.InvalidPhoneOrPasswordException;
import com.example.testtask.repository.UserRepository;
import com.example.testtask.security.JwUtil;
import com.example.testtask.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwUtil jwUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String authenticateByEmail(String email, String password) {
        log.info("Попытка аутентификации по email: {}", email);

        User user = userRepository.findByEmails_Email(email)
                .orElseThrow(() -> {
                    log.warn("Аутентификация неудачна — пользователь с email '{}' не найден", email);
                    return new InvalidEmailOrPasswordException("Неверный email или пароль");
                });
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Неверный пароль для email '{}'", email);
            throw new InvalidEmailOrPasswordException("Неверный email или пароль");
        }

        String token = jwUtil.generateToken(user.getId());
        log.info("Успешная аутентификация по email: {}", email);

        return token;
    }

    @Override
    public String authenticateByPhone(String phone, String password) {
        log.info("Попытка аутентификации по номеру телефона : {}", phone);

        User user = userRepository.findByPhones_Phone(phone)
                .orElseThrow(() -> {
                    log.warn("Аутентификация неудачна - пользователь с таким номером телефона '{}' не найден", phone);
                    return new InvalidPhoneOrPasswordException("Неверный номер телефона или пароль");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Неверный пароль для номера телефона '{}'", phone);
            throw new InvalidPhoneOrPasswordException("Неверный номер телефона или пароль");
        }
        String token = jwUtil.generateToken(user.getId());
        log.info("Успешная аутентификация по номеру телефона {}", phone);

        return token;
    }
}
