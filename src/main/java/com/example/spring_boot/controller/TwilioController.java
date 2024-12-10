package com.example.spring_boot.controller;

import java.time.ZonedDateTime;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_boot.entities.SmsMessage;
import com.example.spring_boot.service.TwilioService;

@RestController
@RequestMapping("/sms")
public class TwilioController {

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody SmsMessage smsMessage) {
        try {
            String ssid = twilioService.sendMessage(smsMessage.getTo(), smsMessage.getBody());
            return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body(ssid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }

    @PostMapping("/incoming")
    public void receiveMessage(
        @RequestParam("From") String from,
        @RequestParam("To") String to,
        @RequestParam("Body") String body,
        @RequestParam("MessageSid") String messageSid
        ) {
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setFrom(from);
        smsMessage.setTo(to);
        smsMessage.setBody(body);
        smsMessage.setMessageSid(messageSid);
        smsMessage.setSentAt(ZonedDateTime.now());
        smsMessage.setStatus("delivered");

        twilioService.saveMessage(smsMessage);
    }
}