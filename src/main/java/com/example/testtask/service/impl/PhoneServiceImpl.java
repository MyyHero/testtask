package com.example.testtask.service.impl;

import com.example.testtask.dto.request.CreatePhoneRequest;
import com.example.testtask.dto.request.UpdatePhoneRequest;
import com.example.testtask.dto.response.PhoneResponse;
import com.example.testtask.entity.PhoneData;
import com.example.testtask.entity.User;
import com.example.testtask.exception.AccessDeniedException;
import com.example.testtask.exception.PhoneNumberAlreadyExistsException;
import com.example.testtask.exception.PhoneNumberNotFoundException;
import com.example.testtask.mapper.PhoneMapper;
import com.example.testtask.repository.PhoneDataRepository;
import com.example.testtask.security.SecurityUtil;
import com.example.testtask.service.PhoneService;
import com.example.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhoneServiceImpl implements PhoneService {

    private final PhoneDataRepository phoneRepository;
    private final UserService userService;
    private final PhoneMapper phoneMapper;

    @Transactional
    @Override
    @CacheEvict(value = "userPhones", key = "'getUserPhones_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    public PhoneResponse addPhone(CreatePhoneRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        if (phoneRepository.existsByPhone(request.getPhone())) {
            log.warn("Попытка добавить уже существующий номер телефона '{}'", request.getPhone());
            throw new PhoneNumberAlreadyExistsException("Этот номер телефона уже используется");
        }

        User user = userService.getById(userId);
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(request.getPhone());
        phoneData.setUser(user);
        PhoneData saved = phoneRepository.save(phoneData);

        log.info("Телефон {} добавлен для пользователя ID={}", saved.getPhone(), userId);
        return phoneMapper.toDto(saved);
    }

    @Transactional
    @Override
    @CacheEvict(value = "userPhones", key = "'getUserPhones_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    public void deletePhone(Long phoneId) {
        Long userId = SecurityUtil.getCurrentUserId();

        PhoneData phoneData = phoneRepository.findById(phoneId)
                .orElseThrow(() -> {
                    log.warn("Телефон ID={} не найден при удалении пользователем ID={}", phoneId, userId);
                    return new PhoneNumberNotFoundException("Номер телефона не найден");
                });

        if (!Objects.equals(phoneData.getUser().getId(), userId)) {
            log.warn("Попытка удалить чужой телефон. Phone ID={}, User ID={}", phoneId, userId);
            throw new AccessDeniedException("Вы не можете удалить телефон другого пользователя");
        }

        phoneRepository.delete(phoneData);
        log.info("Телефон ID={} успешно удалён пользователем ID={}", phoneId, userId);
    }

    @Transactional
    @Override
    @CacheEvict(value = "userPhones", key = "'getUserPhones_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    public PhoneResponse updatePhone(Long phoneId, UpdatePhoneRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        PhoneData phoneData = phoneRepository.findById(phoneId)
                .orElseThrow(() -> {
                    log.warn("Телефон ID={} не найден при обновлении пользователем ID={}", phoneId, userId);
                    return new PhoneNumberNotFoundException("Телефон не найден");
                });

        if (!Objects.equals(phoneData.getUser().getId(), userId)) {
            log.warn("Попытка изменить чужой телефон. Phone ID={}, User ID={}", phoneId, userId);
            throw new AccessDeniedException("Вы не можете изменить телефон другого пользователя");
        }

        if (Objects.equals(phoneData.getPhone(), request.getNewPhone())) {
            log.info("Попытка обновить телефон на тот же самый: '{}'. Изменений не требуется. User ID={}", request.getNewPhone(), userId);
            return phoneMapper.toDto(phoneData);
        }

        if (phoneRepository.existsByPhone(request.getNewPhone())) {
            log.warn("Попытка установить номер телефона '{}', который уже используется другим пользователем. User ID={}", request.getNewPhone(), userId);
            throw new PhoneNumberAlreadyExistsException("Этот телефон уже используется другим пользователем");
        }

        phoneData.setPhone(request.getNewPhone());
        log.info("Телефон ID={} обновлён пользователем ID={} на {}", phoneId, userId, request.getNewPhone());

        return phoneMapper.toDto(phoneData);
    }

    @Override
    @Cacheable(value = "userPhones", key = "#root.methodName + '_' + T(com.example.testtask.security.SecurityUtil).getCurrentUserId()")
    public List<PhoneResponse> getUserPhones() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<PhoneData> phones = phoneRepository.findAllByUserId(userId);
        log.info("Получен список телефонов пользователя ID={}, количество={}", userId, phones.size());
        return phones.stream().map(phoneMapper::toDto).toList();
    }

    @Override
    public boolean isTakenForAnotherUser(String phone, Long currentUserId) {
        return phoneRepository.findByPhone(phone)
                .filter(p -> !Objects.equals(p.getUser().getId(), currentUserId))
                .isPresent();
    }
}
