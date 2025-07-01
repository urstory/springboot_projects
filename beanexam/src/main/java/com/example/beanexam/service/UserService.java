package com.example.beanexam.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 * @Service 어노테이션을 사용한 Bean 정의 예시
 * Singleton 스코프로 관리되는 서비스 Bean
 */
@Slf4j
@Service
public class UserService {
    
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<String> users = new ArrayList<>();
    
    /**
     * Bean 초기화 메서드
     * @PostConstruct 어노테이션을 통해 Bean 생성 후 자동 실행
     */
    @PostConstruct
    public void initialize() {
        log.info("🔵 UserService Bean이 초기화되었습니다.");
        users.add("Alice");
        users.add("Bob");
        users.add("Charlie");
        log.info("🔵 기본 사용자 {} 명이 추가되었습니다.", users.size());
    }
    
    /**
     * Bean 소멸 메서드
     * @PreDestroy 어노테이션을 통해 Bean 소멸 전 자동 실행
     */
    @PreDestroy
    public void cleanup() {
        log.info("🔴 UserService Bean이 소멸됩니다. 정리 작업을 수행합니다.");
        users.clear();
    }
    
    public List<String> getAllUsers() {
        log.debug("모든 사용자 조회: {}", users);
        return new ArrayList<>(users);
    }
    
    public String addUser(String userName) {
        String newUser = String.format("%s-%d", userName, idGenerator.getAndIncrement());
        users.add(newUser);
        log.info("새 사용자 추가: {}", newUser);
        return newUser;
    }
    
    public String getUserServiceInfo() {
        return String.format("UserService Bean - 관리 중인 사용자 수: %d, Bean 해시코드: %d", 
                           users.size(), this.hashCode());
    }
} 