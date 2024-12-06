package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring_boot.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}