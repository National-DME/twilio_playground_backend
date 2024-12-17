package com.example.spring_boot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;

import jakarta.annotation.PostConstruct;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.spring_boot.repository.TwilioRepository;
import com.example.spring_boot.entities.SmsMedia;
import com.example.spring_boot.entities.SmsMessage;

// Service class; Used to handle business logic around twilio's API
@Service 
public class TwilioService {

    // Repository that handles storing twilio data into local database
    @Autowired
    private TwilioRepository twilioRepository;

    // Environment variables to access twilio API
    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    // Initializing twilio API interface
    @PostConstruct
    public void init() {
        if (accountSid != null && authToken != null) {
            Twilio.init(accountSid, authToken);
        } else {
            throw new IllegalStateException("Twilio credentials are not set!");
        }
    }

    // Called from controller classes, sends an SMS MESSAGE via twilio
    public String sendMessage(String to, String body, List<SmsMedia> media) {
        MessageCreator messageCreator = Message.creator(
            new com.twilio.type.PhoneNumber(to),
            new com.twilio.type.PhoneNumber(twilioPhoneNumber),
            body
        );

        if (media != null && !media.isEmpty()) {
            List<URI> mediaUrls = media.stream()
                .map(smsMedia -> URI.create(smsMedia.getMediaUrl()))
                .collect(Collectors.toList());

            messageCreator.setMediaUrl(mediaUrls);
        }

        Message message = messageCreator.create();

        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setFrom(twilioPhoneNumber);
        smsMessage.setTo(to);
        smsMessage.setBody(body);
        smsMessage.setSentAt(message.getDateCreated());
        smsMessage.setStatus(message.getStatus().toString());
        smsMessage.setMessageSid(message.getSid());
        // Linking media to sms message
        smsMessage.setMedia(media);

        // Linking sms message to media foreign key
        if (media != null && !media.isEmpty()) {
            for (SmsMedia smsMedia : media) {
                smsMedia.setSmsMessage(smsMessage);
            }
        }

        twilioRepository.save(smsMessage);

        return message.getSid();
    }

    // Stores just an SMS to the messages table via the JPA repository
    public void saveMessage(SmsMessage smsMessage) {
        twilioRepository.save(smsMessage);
    }
}