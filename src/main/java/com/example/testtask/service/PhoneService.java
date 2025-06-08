package com.example.testtask.service;


import com.example.testtask.dto.request.CreatePhoneRequest;
import com.example.testtask.dto.request.UpdatePhoneRequest;
import com.example.testtask.dto.response.PhoneResponse;

import java.util.List;

public interface PhoneService {
    PhoneResponse addPhone(CreatePhoneRequest createPhoneRequest);

    void deletePhone(Long phoneId);

    PhoneResponse updatePhone(Long phoneId, UpdatePhoneRequest updatePhoneRequest);

    List<PhoneResponse> getUserPhones();

    boolean isTakenForAnotherUser(String phone, Long currentUserId);

}
