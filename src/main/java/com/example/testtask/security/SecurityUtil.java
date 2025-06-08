package com.example.testtask.security;

import com.example.testtask.exception.InvalidAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Попытка доступа без аутентификации");
            throw new InvalidAuthenticationException("Пользователь не аутентифицирован");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        log.warn("Неподдерживаемый тип principal: {}", principal.getClass().getName());
        throw new InvalidAuthenticationException("Некорректный principal в JWT токене");
    }

}
