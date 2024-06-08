package com.ecommerce.notificationservice.service;

public interface SmsService {

    void sendSms(String to, String message);
}
