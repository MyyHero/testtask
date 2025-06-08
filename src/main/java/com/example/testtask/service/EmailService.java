package com.example.testtask.service;

import com.example.testtask.dto.request.CreateEmailRequest;
import com.example.testtask.dto.request.UpdateEmailRequest;
import com.example.testtask.dto.response.EmailResponse;

import java.util.List;

public interface EmailService {
    EmailResponse addEmail(CreateEmailRequest request);
    void deleteEmail(Long emailId);
    EmailResponse updateEmail(Long emailId, UpdateEmailRequest updateEmailRequest);
    List<EmailResponse> getUserEmails();
    boolean isTakenForAnotherUser(String email, Long currentUserId);

}
