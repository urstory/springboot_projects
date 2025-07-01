package com.example.beanexam.service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Component 어노테이션을 사용한 Bean 정의 예시
 * 일반적인 컴포넌트로서의 Bean
 */
@Slf4j
@Component
public class EmailService {
    
    private final AtomicInteger emailCount = new AtomicInteger(0);
    
    public String sendEmail(String to, String subject, String content) {
        int count = emailCount.incrementAndGet();
        String emailId = String.format("EMAIL-%04d", count);
        
        log.info("📧 이메일 발송 - ID: {}, 수신자: {}, 제목: {}", emailId, to, subject);
        
        // 실제로는 이메일 발송 로직이 들어갈 부분
        // 여기서는 시뮬레이션만 수행
        
        return String.format("이메일 발송 완료 - ID: %s, 시간: %s", emailId, LocalDateTime.now());
    }
    
    public String getEmailServiceStatus() {
        return String.format("EmailService 상태 - 총 발송 이메일 수: %d, Bean 해시코드: %d",
                           emailCount.get(), this.hashCode());
    }
} 