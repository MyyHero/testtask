package com.example.testtask.service.impl;

import com.example.testtask.dto.request.CreateEmailRequest;
import com.example.testtask.dto.request.UpdateEmailRequest;
import com.example.testtask.dto.response.EmailResponse;
import com.example.testtask.entity.EmailData;
import com.example.testtask.entity.User;
import com.example.testtask.exception.AccessDeniedException;
import com.example.testtask.exception.EmailAlreadyExistsException;
import com.example.testtask.exception.EmailNotFoundException;
import com.example.testtask.exception.InvalidNumberOfEmails;
import com.example.testtask.mapper.EmailMapper;
import com.example.testtask.repository.EmailDataRepository;
import com.example.testtask.security.SecurityUtil;
import com.example.testtask.service.EmailService;
import com.example.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailServiceImpl implements EmailService {

    private final EmailDataRepository emailRepository;
    private final UserService userService;
    private final EmailMapper emailMapper;

    @Transactional
    @Override
    @CacheEvict(value = "userEmails", key = "'getUserEmails_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    public EmailResponse addEmail(CreateEmailRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        if (emailRepository.existsByEmail(request.getEmail())) {
            log.warn("Попытка добавить уже существующий email '{}'", request.getEmail());
            throw new EmailAlreadyExistsException("Этот email уже используется другим пользователем");
        }

        User user = userService.getById(userId);
        EmailData emailData = new EmailData();
        emailData.setEmail(request.getEmail());
        emailData.setUser(user);
        EmailData saved = emailRepository.save(emailData);
        log.info("Email {} добавлен для пользователя ID={}", saved.getEmail(), userId);

        return emailMapper.toDto(saved);
    }

    @CacheEvict(value = "userEmails", key = "'getUserEmails_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    @Transactional
    @Override
    public void deleteEmail(Long emailId) {
        Long userId = SecurityUtil.getCurrentUserId();
        EmailData emailData = emailRepository.findById(emailId)
                .orElseThrow(() -> {
                    log.warn("Email ID={} не найден при удалении пользователем ID={}", emailId, userId);
                    return new EmailNotFoundException("Email не найден");
                });

        if (!emailData.getUser().getId().equals(userId)) {
            log.warn("Попытка удалить email другим пользователем. Email ID={}, User ID={}", emailId, userId);
            throw new AccessDeniedException("Вы не можете удалить email другого пользователя");
        }
        if (emailRepository.countByUser_Id(userId) == 1)
            throw new InvalidNumberOfEmails("Нельзя удалить последний e-mail");


        emailRepository.delete(emailData);
        log.info("Email ID={} удалён пользователем ID={}", emailId, userId);
    }

    @CacheEvict(value = "userEmails", key = "'getUserEmails_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    @Transactional
    @Override
    public EmailResponse updateEmail(Long emailId, UpdateEmailRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        EmailData emailData = emailRepository.findById(emailId)
                .orElseThrow(() -> {
                    log.warn("Email ID={} не найден при обновлении пользователем ID={}", emailId, userId);
                    return new EmailNotFoundException("Email не найден");
                });

        if (!emailData.getUser().getId().equals(userId)) {
            log.warn("Попытка изменить email другим пользователем. Email ID={}, User ID={}", emailId, userId);
            throw new AccessDeniedException("Вы не можете изменить email другого пользователя");
        }

        if (Objects.equals(emailData.getEmail(), request.getNewEmail())) {
            log.info("Попытка обновить email на тот же самый: '{}'. Изменений не требуется. User ID={}", request.getNewEmail(), userId);
            return emailMapper.toDto(emailData);
        }
        if (emailRepository.existsByEmail(request.getNewEmail())) {
            log.warn("Попытка установить email '{}', который уже используется другим пользователем. User ID={}", request.getNewEmail(), userId);
            throw new EmailAlreadyExistsException("Этот email уже используется другим пользователем");
        }

        emailData.setEmail(request.getNewEmail());
        log.info("Email ID={} обновлён пользователем ID={} на {}", emailId, userId, request.getNewEmail());

        return emailMapper.toDto(emailData);
    }

    @Cacheable(value = "userEmails", key = "#root.methodName + '_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    @Override
    public List<EmailResponse> getUserEmails() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<EmailData> emails = emailRepository.findAllByUserId(userId);
        log.info("Получен список email'ов пользователя ID={}, количество={}", userId, emails.size());
        return emails.stream().map(emailMapper::toDto).toList();
    }

    @Override
    public boolean isTakenForAnotherUser(String email, Long currentUserId) {
        return emailRepository.findByEmail(email)
                .filter(e -> !Objects.equals(e.getUser().getId(), currentUserId))
                .isPresent();
    }
}
