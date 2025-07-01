package com.example.beanexam.runner;

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 애플리케이션 시작 시 Bean 정보를 검사하고 출력하는 Runner
 * CommandLineRunner 인터페이스를 구현하여 자동 실행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BeanInspectionRunner implements CommandLineRunner {
    
    private final ApplicationContext applicationContext;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("\n" + "=".repeat(80));
        log.info("🔍 Spring Bean 검사 시작");
        log.info("=".repeat(80));
        
        inspectBeanCount();
        inspectCustomBeans();
        inspectBeansByType();
        
        log.info("=".repeat(80));
        log.info("🔍 Spring Bean 검사 완료");
        log.info("=".repeat(80) + "\n");
    }
    
    /**
     * 전체 Bean 개수 확인
     */
    private void inspectBeanCount() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        log.info("📊 전체 Bean 개수: {}", beanNames.length);
        
        // 우리가 만든 커스텀 Bean들만 필터링
        long customBeanCount = Arrays.stream(beanNames)
                .filter(name -> name.contains("beanexam") || 
                               name.startsWith("user") || 
                               name.startsWith("email") || 
                               name.startsWith("prototype"))
                .count();
        
        log.info("📊 커스텀 Bean 개수: {}", customBeanCount);
    }
    
    /**
     * 커스텀 Bean들 상세 정보 출력
     */
    private void inspectCustomBeans() {
        log.info("\n📋 커스텀 Bean 목록:");
        
        String[] customBeanNames = {
            "userService", "prototypeService", "emailService", 
            "userRepository", "appConfig", "customBeanConfig",
            "primaryConfigurationService", "secondaryConfigurationService",
            "dateTimeFormatter", "applicationSettings"
        };
        
        for (String beanName : customBeanNames) {
            try {
                if (applicationContext.containsBean(beanName)) {
                    Object bean = applicationContext.getBean(beanName);
                    String scope = applicationContext.isPrototype(beanName) ? "prototype" : "singleton";
                    
                    log.info("  🔸 {} - 타입: {}, 스코프: {}, 해시코드: {}", 
                           beanName, 
                           bean.getClass().getSimpleName(),
                           scope,
                           bean.hashCode());
                }
            } catch (Exception e) {
                log.debug("Bean '{}' 조회 실패: {}", beanName, e.getMessage());
            }
        }
    }
    
    /**
     * 타입별 Bean 분류
     */
    private void inspectBeansByType() {
        log.info("\n📊 Bean 타입별 분류:");
        
        try {
            // Service 타입 Bean들
            Map<String, Object> serviceBeans = applicationContext.getBeansWithAnnotation(
                org.springframework.stereotype.Service.class);
            log.info("  🔹 @Service Bean: {} 개", serviceBeans.size());
            serviceBeans.forEach((name, bean) -> 
                log.info("    - {}: {}", name, bean.getClass().getSimpleName()));
        } catch (Exception e) {
            log.warn("  ⚠️ @Service Bean 검사 중 오류 발생 (Request/Session scope bean 포함): {}", e.getMessage());
        }
        
        try {
            // Repository 타입 Bean들
            Map<String, Object> repositoryBeans = applicationContext.getBeansWithAnnotation(
                org.springframework.stereotype.Repository.class);
            log.info("  🔹 @Repository Bean: {} 개", repositoryBeans.size());
            repositoryBeans.forEach((name, bean) -> 
                log.info("    - {}: {}", name, bean.getClass().getSimpleName()));
        } catch (Exception e) {
            log.warn("  ⚠️ @Repository Bean 검사 중 오류 발생: {}", e.getMessage());
        }
        
        try {
            // Component 타입 Bean들 (Request/Session scope bean 제외)
            log.info("  🔹 @Component Bean 검사 (singleton/prototype만):");
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            int componentCount = 0;
            
            for (String beanName : beanNames) {
                try {
                    if (applicationContext.containsBean(beanName)) {
                        // Request/Session scope bean은 건너뛰기
                        if (beanName.contains("request") || beanName.contains("session") || 
                            beanName.contains("RequestBean") || beanName.contains("SessionBean")) {
                            log.debug("    - {} (Request/Session scope - 건너뜀)", beanName);
                            continue;
                        }
                        
                        Object bean = applicationContext.getBean(beanName);
                        if (bean.getClass().isAnnotationPresent(org.springframework.stereotype.Component.class) &&
                            beanName.contains("beanexam")) {
                            log.info("    - {}: {}", beanName, bean.getClass().getSimpleName());
                            componentCount++;
                        }
                    }
                } catch (Exception e) {
                    log.debug("    - {} (접근 불가: {})", beanName, e.getMessage());
                }
            }
            log.info("  🔹 @Component Bean: {} 개 (Request/Session scope 제외)", componentCount);
            
        } catch (Exception e) {
            log.warn("  ⚠️ @Component Bean 검사 중 오류 발생: {}", e.getMessage());
        }
        
        try {
            // Configuration 타입 Bean들
            Map<String, Object> configBeans = applicationContext.getBeansWithAnnotation(
                org.springframework.context.annotation.Configuration.class);
            log.info("  🔹 @Configuration Bean: {} 개", configBeans.size());
            configBeans.entrySet().stream()
                .filter(entry -> entry.getKey().contains("beanexam") || entry.getKey().contains("Config"))
                .forEach(entry -> 
                    log.info("    - {}: {}", entry.getKey(), entry.getValue().getClass().getSimpleName()));
        } catch (Exception e) {
            log.warn("  ⚠️ @Configuration Bean 검사 중 오류 발생: {}", e.getMessage());
        }
        
        // Request/Session scope bean 정보 출력
        log.info("  🔹 Request/Session Scope Bean:");
        log.info("    - requestBean: RequestBean (Request scope - 웹 요청시에만 접근 가능)");
        log.info("    - sessionBean: SessionBean (Session scope - 웹 세션시에만 접근 가능)");
        log.info("    ℹ️ Request/Session scope bean은 CommandLineRunner에서 접근할 수 없으므로 웹 API를 통해 테스트하세요.");
    }
} 