package com.example.spring_boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.spring_boot.entities.User;
import com.example.spring_boot.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public void createUser(@RequestBody User user) {
        userService.createUser(user.getName(), user.getPhoneNumber());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}