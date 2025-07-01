package com.example.beanexam.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Prototype 스코프 Bean 예시
 * 매번 새로운 인스턴스가 생성되는 Bean
 */
@Slf4j
@Service
@Scope("prototype")
public class PrototypeService {
    
    private final String instanceId;
    private final LocalDateTime createdAt;
    
    public PrototypeService() {
        this.instanceId = UUID.randomUUID().toString().substring(0, 8);
        this.createdAt = LocalDateTime.now();
        log.info("🟢 새로운 PrototypeService 인스턴스 생성: {}", instanceId);
    }
    
    @PostConstruct
    public void initialize() {
        log.info("🟢 PrototypeService 인스턴스 {} 초기화 완료", instanceId);
    }
    
    public String getInstanceInfo() {
        return String.format("PrototypeService - ID: %s, 생성시간: %s, 해시코드: %d",
                           instanceId, createdAt, this.hashCode());
    }
    
    public String performTask(String taskName) {
        log.info("인스턴스 {}에서 작업 수행: {}", instanceId, taskName);
        return String.format("작업 '%s'가 인스턴스 %s에서 완료되었습니다.", taskName, instanceId);
    }
} 