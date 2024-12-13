package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring_boot.entities.SmsMedia;

public interface MediaRepository extends JpaRepository<SmsMedia, Long> {
    
}
