package com.example.spring_boot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.spring_boot.repository.TwilioRepository;
import com.example.spring_boot.entities.SmsMessage;

@Service 
public class TwilioService {

    @Autowired
    private TwilioRepository twilioRepository;

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && authToken != null) {
            Twilio.init(accountSid, authToken);
        } else {
            throw new IllegalStateException("Twilio credentials are not set!");
        }
    }

    public String sendMessage(String to, String body) {
        Message message = Message.creator(
            new com.twilio.type.PhoneNumber(to),
            new com.twilio.type.PhoneNumber(twilioPhoneNumber),
            body
        ).create();
        return message.getSid();
    }

    public void saveMessage(String from, String body) {
        SmsMessage smsMessage = new SmsMessage(from, body);
        twilioRepository.save(smsMessage);
    }
}