package com.ecommerce.notificationservice.service;

public interface EmailService {

    void sendMail(String to, String subject, String message);
}
