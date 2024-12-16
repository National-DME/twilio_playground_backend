package com.example.spring_boot.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.spring_boot.repository.UserRepository;
import com.example.spring_boot.entities.User;
import java.util.List;

// Service class; used to test springboot instance
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(String name, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
    }
}