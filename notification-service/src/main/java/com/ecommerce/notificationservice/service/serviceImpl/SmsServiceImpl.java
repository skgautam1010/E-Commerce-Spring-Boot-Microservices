package com.ecommerce.notificationservice.service.serviceImpl;

import com.ecommerce.notificationservice.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    @Value("${twilio.from-number}")
    private String fromNumber;


    @Override
    public void sendSms(String to, String message) {
        Message.creator(new PhoneNumber(to), new PhoneNumber(fromNumber), message).create();
    }
}
