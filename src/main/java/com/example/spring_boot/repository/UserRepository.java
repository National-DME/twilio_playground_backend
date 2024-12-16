package com.example.spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring_boot.entities.User;

// User repository; I am currently just using the generated CRUD operations from JPA (springboot database interface that automatically generates CRUD queries); called in service classes
public interface UserRepository extends JpaRepository<User, Long> {
    
}