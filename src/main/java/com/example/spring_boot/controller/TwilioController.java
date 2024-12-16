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
import com.example.spring_boot.service.MediaService;
import com.example.spring_boot.service.TwilioService;

import io.jsonwebtoken.io.IOException;

// This file is the controller
// Represents http://164.92.69.32:8080/sms
// Able to send a request to base url /sms/incoming to receive
// Able to send out by pinging /sms/send
@RestController
@RequestMapping("/sms")
public class TwilioController {

    // Dependency used for logging in the server terminal
    private static final Logger logger = LoggerFactory.getLogger(TwilioController.class);

    // Initializing service class (used to communicate with twilio)
    @Autowired
    private TwilioService twilioService;

    // Initialing media service class (used to handle media files)
    @Autowired
    private MediaService mediaService;
    
    // Send endpoint
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody SmsMessage smsMessage) {
        try {
            String ssid = twilioService.sendMessage(smsMessage.getTo(), smsMessage.getBody());
            return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body(ssid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }

    // Incoming endpoint (used to handle incoming MMS and SMS messages)
    @PostMapping("/incoming")
    public void receiveMessage(
        @RequestParam("From") String from,
        @RequestParam("To") String to,
        @RequestParam("Body") String body,
        @RequestParam("MessageSid") String messageSid,
        @RequestParam(value = "NumMedia", required = false, defaultValue = "0") int numMedia,
        @RequestParam Map<String, String> allParams
        ) throws java.io.IOException {

        logger.info("Received message");
        logger.info(numMedia + " files to process");

        // Create an array to hold media if any
        List<SmsMedia> mediaList = new ArrayList<>();
        
        // Create an smsMessage instance
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setFrom(from);
        smsMessage.setTo(to);
        smsMessage.setBody(body);
        smsMessage.setMessageSid(messageSid);
        smsMessage.setSentAt(ZonedDateTime.now());
        smsMessage.setStatus("delivered");
        
        // Loop through media
        // When twilio sends MME to your webhook, they send a new parameter for each media file sent; named MediaUrl0, MediaUrl1 etc...
        // They send the content type for each image in the same format
        for (int i = 0; i < numMedia; i++) {
            // For each media file sent from user
            // Twilio URL is mediaUrl
            String mediaUrl = allParams.get("MediaUrl" + i);
            String mediaContentType = allParams.get("MediaContentType" + i);

            if (mediaUrl != null && mediaContentType != null) {
                try {
                    // Initiate new media instance
                    SmsMedia media = new SmsMedia();

                    // Download and return local image url to link to the sms message as a foreign key
                    String localMediaUrl = mediaService.saveMedia(mediaUrl, mediaContentType);
                    media.setMediaUrl(localMediaUrl);
                    media.setMediaContentType(mediaContentType);
                    media.setSmsMessage(smsMessage);
                    mediaList.add(media);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        // Save message and media in sms messages and sms media tables
        smsMessage.setMedia(mediaList);
        twilioService.saveMessage(smsMessage);
    }
}