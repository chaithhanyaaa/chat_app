package com.chatapp.backend.dto;

public class AuthResponse {

    private String token;
    private long userId;

    public AuthResponse(String token,long userId)
    {
        this.userId=userId;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public long getUserId()
    {
        return userId;
    }
}