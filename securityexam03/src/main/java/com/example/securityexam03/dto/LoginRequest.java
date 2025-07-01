package com.example.securityexam03.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 로그인 요청 DTO
 */
public class LoginRequest {
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
} 