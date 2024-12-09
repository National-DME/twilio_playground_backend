package com.example.spring_boot.dto;

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
