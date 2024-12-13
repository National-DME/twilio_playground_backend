package com.example.spring_boot.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_boot.entities.SmsMedia;
import com.example.spring_boot.entities.SmsMessage;
import com.example.spring_boot.service.TwilioService;

@RestController
@RequestMapping("/sms")
public class TwilioController {

    private static final Logger logger = LoggerFactory.getLogger(TwilioController.class);

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
        @RequestParam("MessageSid") String messageSid,
        @RequestParam(value = "NumMedia", required = false, defaultValue = "0") int numMedia,
        @RequestParam Map<String, String> allParams
        ) {

        logger.info("Received message");
        logger.info(numMedia + " files to process");

        List<SmsMedia> mediaList = new ArrayList<>();
        
        for (int i = 0; i < numMedia; i++) {
            String mediaUrl = allParams.get("MediaUrl" + i);
            String mediaContentType = allParams.get("MediaContentType" + i);

            if (mediaUrl != null && mediaContentType != null) {
                SmsMedia media = new SmsMedia();
                media.setMediaUrl(mediaUrl);
                media.setMediaContentType(mediaContentType);
                mediaList.add(media);
            }
        }

        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setFrom(from);
        smsMessage.setTo(to);
        smsMessage.setBody(body);
        smsMessage.setMessageSid(messageSid);
        smsMessage.setSentAt(ZonedDateTime.now());
        smsMessage.setStatus("delivered");
        smsMessage.setMedia(mediaList);

        twilioService.saveMessage(smsMessage);
    }
}