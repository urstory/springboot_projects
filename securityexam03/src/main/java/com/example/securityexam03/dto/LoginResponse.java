package com.example.securityexam03.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 로그인 응답 DTO
 */
public class LoginResponse {
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("roles")
    private List<String> roles;
    
    @JsonProperty("tokenType")
    private String tokenType = "Bearer";
    
    @JsonProperty("expiresAt")
    private Date expiresAt;
    
    @JsonProperty("message")
    private String message;

    public LoginResponse() {}

    public LoginResponse(String token, String username, List<String> roles, Date expiresAt) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.expiresAt = expiresAt;
        this.message = "로그인 성공";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "username='" + username + '\'' +
                ", roles=" + roles +
                ", tokenType='" + tokenType + '\'' +
                ", expiresAt=" + expiresAt +
                ", message='" + message + '\'' +
                '}';
    }
} 