package com.example.filterexam.filter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 성능 측정을 위한 Filter
 * 요청 처리 시간 측정 및 성능 통계 수집
 */
@Slf4j
@Component
@Order(2)  // LoggingFilter 다음에 실행
public class PerformanceFilter implements Filter {

    @Value("${app.filter.performance.slow-request-threshold:1000}")
    private long slowRequestThreshold;

    // 성능 통계 저장
    private final ConcurrentHashMap<String, PerformanceStats> statsMap = new ConcurrentHashMap<>();
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalProcessingTime = new AtomicLong(0);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("🔵 PerformanceFilter 초기화됨 - 느린 요청 임계값: {}ms", slowRequestThreshold);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String endpoint = httpRequest.getMethod() + " " + httpRequest.getRequestURI();
        long startTime = System.currentTimeMillis();

        try {
            // 다음 필터 또는 컨트롤러로 요청 전달
            chain.doFilter(request, response);
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            
            // 성능 통계 업데이트
            updatePerformanceStats(endpoint, processingTime);
            
            // 전체 통계 업데이트
            totalRequests.incrementAndGet();
            totalProcessingTime.addAndGet(processingTime);
            
            // 성능 로그
            logPerformance(endpoint, processingTime, httpResponse.getStatus());
        }
    }

    private void updatePerformanceStats(String endpoint, long processingTime) {
        statsMap.compute(endpoint, (key, stats) -> {
            if (stats == null) {
                stats = new PerformanceStats();
            }
            stats.addRequest(processingTime);
            return stats;
        });
    }

    private void logPerformance(String endpoint, long processingTime, int status) {
        if (processingTime > slowRequestThreshold) {
            log.warn("🐌 느린 요청: {} - {}ms (상태: {})", endpoint, processingTime, status);
        } else {
            log.debug("⚡ 요청 처리: {} - {}ms (상태: {})", endpoint, processingTime, status);
        }
    }

    /**
     * 성능 통계 조회
     */
    public ConcurrentHashMap<String, PerformanceStats> getPerformanceStats() {
        return new ConcurrentHashMap<>(statsMap);
    }

    /**
     * 전체 평균 응답시간 조회
     */
    public long getAverageResponseTime() {
        long total = totalRequests.get();
        return total > 0 ? totalProcessingTime.get() / total : 0;
    }

    /**
     * 총 요청 수 조회
     */
    public long getTotalRequests() {
        return totalRequests.get();
    }

    @Override
    public void destroy() {
        log.info("🔴 PerformanceFilter 소멸됨");
        log.info("📊 최종 성능 통계:");
        log.info("   총 요청 수: {}", totalRequests.get());
        log.info("   평균 응답시간: {}ms", getAverageResponseTime());
        
        statsMap.forEach((endpoint, stats) -> {
            log.info("   {}: 요청수={}, 평균={}ms, 최대={}ms, 최소={}ms",
                    endpoint, stats.getRequestCount(), stats.getAverageTime(),
                    stats.getMaxTime(), stats.getMinTime());
        });
    }

    /**
     * 엔드포인트별 성능 통계 클래스
     */
    public static class PerformanceStats {
        private long requestCount = 0;
        private long totalTime = 0;
        private long maxTime = 0;
        private long minTime = Long.MAX_VALUE;

        public synchronized void addRequest(long processingTime) {
            requestCount++;
            totalTime += processingTime;
            maxTime = Math.max(maxTime, processingTime);
            minTime = Math.min(minTime, processingTime);
        }

        public long getRequestCount() { return requestCount; }
        public long getAverageTime() { return requestCount > 0 ? totalTime / requestCount : 0; }
        public long getMaxTime() { return maxTime; }
        public long getMinTime() { return minTime == Long.MAX_VALUE ? 0 : minTime; }
        public long getTotalTime() { return totalTime; }
    }
} 