package com.example.beanexam.model;

/**
 * @Bean 방식으로 등록할 예제 서비스 클래스
 * 내용.md 5.1절 예제
 */
public class MyService {
    private String message;

    public MyService(String message) {
        this.message = message;
    }

    public void printMessage() {
        System.out.println("Message: " + message);
    }

    public String getMessage() {
        return message;
    }
} 