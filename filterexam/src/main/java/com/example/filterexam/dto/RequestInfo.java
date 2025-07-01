package com.example.filterexam.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP 요청 정보를 담는 DTO 클래스
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {
    private String requestId;
    private String method;
    private String uri;
    private String remoteAddr;
    private String userAgent;
    private Map<String, String> headers;
    private LocalDateTime timestamp;
    private long processingTime;
    private int responseStatus;
    private String filterChain;
    private String interceptorChain;
} 