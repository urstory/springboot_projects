package com.example.beanexam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 기본 제공 Bean을 커스터마이징하는 예시
 * 내용.md 3.7절의 실습 예제
 */
@Slf4j
@Configuration
public class CustomBeanConfig {
    
    /**
     * 커스텀 ObjectMapper Bean 정의
     * 기본 Jackson ObjectMapper를 대체
     */
    @Bean
    public ObjectMapper customObjectMapper() {
        log.info("🔧 커스텀 ObjectMapper Bean을 생성합니다.");
        
        ObjectMapper mapper = new ObjectMapper();
        
        // Java 8 시간 타입 지원
        mapper.registerModule(new JavaTimeModule());
        
        // 날짜를 타임스탬프가 아닌 ISO 형식으로 출력
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 보기 좋게 출력 (들여쓰기)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // null 값 제외
        mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        
        return mapper;
    }
    
    /**
     * 커스텀 MappingJackson2HttpMessageConverter Bean 정의
     * 위에서 정의한 커스텀 ObjectMapper 사용
     */
    @Bean
    public MappingJackson2HttpMessageConverter customJacksonMessageConverter(ObjectMapper objectMapper) {
        log.info("🔧 커스텀 MappingJackson2HttpMessageConverter Bean을 생성합니다.");
        
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        
        return converter;
    }
    
    /**
     * 커스텀 DispatcherServlet Bean 정의 (선택사항)
     * 기본 DispatcherServlet의 동작을 커스터마이징
     */
    @Bean
    public DispatcherServlet customDispatcherServlet() {
        log.info("🔧 커스텀 DispatcherServlet Bean을 생성합니다.");
        
        return new DispatcherServlet() {
            @Override
            protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 요청 처리 전 로깅
                log.debug("🔄 커스텀 DispatcherServlet - 요청 처리: {} {}", 
                         request.getMethod(), request.getRequestURI());
                super.doDispatch(request, response);
            }
        };
    }
} 