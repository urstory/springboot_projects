package com.example.filterexam.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Interceptor를 이용한 로깅
 * Controller 메서드 호출 전후 처리
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "interceptor.startTime";
    private static final String HANDLER_ATTR = "interceptor.handler";

    /**
     * Controller 실행 전 호출
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR, startTime);
        request.setAttribute(HANDLER_ATTR, handler.toString());

        log.info("🔄 [Interceptor] PRE - {} {} -> {}",
                request.getMethod(),
                request.getRequestURI(),
                getHandlerName(handler));

        // 요청 파라미터 로깅
        if (!request.getParameterMap().isEmpty()) {
            log.debug("📋 [Interceptor] 요청 파라미터: {}", request.getParameterMap());
        }

        // true 반환 시 다음 인터셉터나 컨트롤러로 진행
        // false 반환 시 요청 처리 중단
        return true;
    }

    /**
     * Controller 실행 후, View 렌더링 전 호출
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) throws Exception {
        
        long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long processingTime = System.currentTimeMillis() - startTime;

        log.info("🔄 [Interceptor] POST - {} {} - 상태: {}, 처리시간: {}ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                processingTime);

        // ModelAndView 정보 로깅
        if (modelAndView != null) {
            log.debug("📋 [Interceptor] ModelAndView: 뷰={}, 모델크기={}",
                    modelAndView.getViewName(),
                    modelAndView.getModel().size());
        }

        // 응답 헤더에 처리 시간 추가
        response.setHeader("X-Processing-Time", String.valueOf(processingTime));
    }

    /**
     * 요청 완료 후 호출 (뷰 렌더링 완료 후)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                               Exception ex) throws Exception {
        
        long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long totalTime = System.currentTimeMillis() - startTime;

        if (ex != null) {
            log.error("❌ [Interceptor] AFTER (예외 발생) - {} {} - 예외: {}, 총 시간: {}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    ex.getMessage(),
                    totalTime);
        } else {
            log.info("✅ [Interceptor] AFTER (완료) - {} {} - 총 시간: {}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    totalTime);
        }

        // 느린 요청 경고
        if (totalTime > 2000) {
            log.warn("🐌 [Interceptor] 매우 느린 요청: {}ms", totalTime);
        }

        // 리소스 정리
        request.removeAttribute(START_TIME_ATTR);
        request.removeAttribute(HANDLER_ATTR);
    }

    private String getHandlerName(Object handler) {
        String handlerString = handler.toString();
        
        // Controller 메서드 정보 추출
        if (handlerString.contains("#")) {
            String[] parts = handlerString.split("#");
            if (parts.length > 1) {
                String className = parts[0].substring(parts[0].lastIndexOf('.') + 1);
                String methodName = parts[1].split("\\(")[0];
                return className + "." + methodName;
            }
        }
        
        return handlerString;
    }
} 