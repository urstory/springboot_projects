package com.example.datajdbc.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Integer age;
    private LocalDateTime createdAt;
    private Boolean active;

    public User(String username, String email, String fullName, Integer age, Boolean active) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
        this.active = active;
        this.createdAt = LocalDateTime.now();
    }
} 