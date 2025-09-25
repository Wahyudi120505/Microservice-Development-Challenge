package com.example.user_service.service;

public interface EmailService {
    void sendEmail(String to, String subject, String code);
    void verifyEmail(String code);

}
