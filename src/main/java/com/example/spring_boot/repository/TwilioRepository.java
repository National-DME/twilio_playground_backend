package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring_boot.entities.SmsMessage;

public interface TwilioRepository extends JpaRepository<SmsMessage, Long> {
    
}