package com.example.spring_boot.dto;

// Data response class; used to respond to HTTP requests; desgined to extend Base response; is base response with data field to return data of T type
public class DataResponse<T> extends BaseResponse {
    private T data;

    public DataResponse(String status, String message, T data) {
        super(status, message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
