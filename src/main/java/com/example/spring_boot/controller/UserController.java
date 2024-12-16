package com.example.spring_boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_boot.dto.BaseResponse;
import com.example.spring_boot.dto.DataResponse;
import com.example.spring_boot.entities.User;
import com.example.spring_boot.service.UserService;

import java.util.List;

// Used for testing server instance of springboot API (basic users handling)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createUser(@RequestBody User user) {
        try {
            userService.createUser(user.getName(), user.getPhoneNumber());
            BaseResponse response = new BaseResponse("Success", "User created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            BaseResponse errorResponse = new BaseResponse("error", "Error creating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<DataResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            DataResponse<List<User>> response = new DataResponse<>("Success", "All users returned", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<User>> errorResponse = new DataResponse<List<User>>("error", "Error retrieving all users" + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            BaseResponse response = new BaseResponse("Success", "User with ID " + id + " was successfully deleted");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            BaseResponse errorResponse = new BaseResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}