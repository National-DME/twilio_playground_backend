package com.example.spring_boot.dto;

import java.time.LocalDateTime;

// Base response class; used to respond to HTTP requests
public class BaseResponse {
    private String status;
    private String message;
    private LocalDateTime timestamp;
    
    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
