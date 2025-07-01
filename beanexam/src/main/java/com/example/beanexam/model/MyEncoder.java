package com.example.beanexam.model;

/**
 * @Bean 방식으로 등록할 예제 인코더 클래스
 * 내용.md 5.1절 예제
 */
public class MyEncoder {
    private String algorithm;

    public MyEncoder(String algorithm) {
        this.algorithm = algorithm;
    }

    public String encode(String data) {
        return algorithm + " encoded: " + data;
    }

    public String getAlgorithm() {
        return algorithm;
    }
} 