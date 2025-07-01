package com.example.configdemo.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.configdemo.config.AppProperties;
import com.example.configdemo.config.DatabaseProperties;
import com.example.configdemo.service.EnvironmentService;

/**
 * 내용.md 5.3절, 5.4절 설정 예제들을 콘솔에서 실행하는 Runner
 * 애플리케이션 시작 시 현재 설정 정보를 출력합니다.
 */
@Component
public class ConfigurationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationRunner.class);

    private final EnvironmentService environmentService;
    private final AppProperties appProperties;
    private final DatabaseProperties databaseProperties;

    public ConfigurationRunner(EnvironmentService environmentService, 
                              AppProperties appProperties,
                              DatabaseProperties databaseProperties) {
        this.environmentService = environmentService;
        this.appProperties = appProperties;
        this.databaseProperties = databaseProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 내용.md 5.3절, 5.4절 Configuration Demo 시작");
        
        printSeparator("현재 프로파일 정보");
        printProfileInfo();
        
        printSeparator("5.4절 - 프로파일별 설정값");
        printProfileSpecificConfig();
        
        printSeparator("5.3절 - 커스텀 프로퍼티 설정");
        printCustomProperties();
        
        printSeparator("5.3절 - 환경변수 활용 설정");
        printEnvironmentVariables();
        
        printSeparator("5.3절 - List와 Map 구조 설정");
        printCollectionProperties();
        
        log.info("🎯 Configuration Demo 완료");
        log.info("🌐 웹 API 테스트는 http://localhost:{}/api/config/summary 에서 확인하세요", 
                environmentService.getServerPort());
        log.info("📋 다른 프로파일 테스트: java -jar app.jar --spring.profiles.active=dev");
    }

    /**
     * 현재 프로파일 정보 출력
     */
    private void printProfileInfo() {
        log.info("📋 활성화된 프로파일:");
        String[] activeProfiles = environmentService.getActiveProfiles();
        if (activeProfiles.length == 0) {
            log.info("   - (기본 프로파일)");
        } else {
            for (String profile : activeProfiles) {
                log.info("   - {}", profile);
            }
        }
        
        log.info("📋 환경 정보:");
        log.info("   - 애플리케이션 환경: {}", environmentService.getAppEnvironment());
        log.info("   - 서버 포트: {}", environmentService.getServerPort());
        log.info("   - 디버그 모드: {}", environmentService.isDebugMode());
        log.info("   - 캐시 활성화: {}", environmentService.isCacheEnabled());
    }

    /**
     * 5.4절 - 프로파일별 설정값 출력
     */
    private void printProfileSpecificConfig() {
        log.info("📋 프로파일별 커스텀 메시지:");
        log.info("   - {}", environmentService.getCustomMessage());
        
        log.info("📋 환경별 특성:");
        log.info("   - 로컬 환경 여부: {}", environmentService.isLocalEnvironment());
        log.info("   - 운영 환경 여부: {}", environmentService.isProductionEnvironment());
    }

    /**
     * 5.3절 - 커스텀 프로퍼티 정보 출력
     */
    private void printCustomProperties() {
        log.info("📋 애플리케이션 기본 정보:");
        log.info("   - 이름: {}", appProperties.getName());
        log.info("   - 버전: {}", appProperties.getVersion());
        log.info("   - API 키: {}", maskApiKey(appProperties.getApiKey()));
        
        if (appProperties.getTimeout() != null) {
            log.info("📋 타임아웃 설정:");
            log.info("   - 연결 타임아웃: {}ms", appProperties.getTimeout().getConnection());
            log.info("   - 읽기 타임아웃: {}ms", appProperties.getTimeout().getRead());
        }
        
        if (appProperties.getFeatures() != null) {
            log.info("📋 기능 설정:");
            log.info("   - 외부 API 모킹: {}", appProperties.getFeatures().isMockExternalApi());
            log.info("   - 모든 요청 로깅: {}", appProperties.getFeatures().isLogAllRequests());
        }
    }

    /**
     * 5.3절 - 환경변수 활용 설정 출력
     */
    private void printEnvironmentVariables() {
        log.info("📋 데이터베이스 설정 (환경변수 활용):");
        log.info("   - {}", databaseProperties.getConnectionInfo());
        log.info("   - 전체 URL: {}", databaseProperties.getUrl());
        
        log.info("📋 활용된 환경변수:");
        log.info("   - DB_HOST: {}", System.getenv().getOrDefault("DB_HOST", "localhost (기본값)"));
        log.info("   - DB_PORT: {}", System.getenv().getOrDefault("DB_PORT", "3306 (기본값)"));
        log.info("   - DB_NAME: {}", System.getenv().getOrDefault("DB_NAME", "configdemo (기본값)"));
        log.info("   - DB_USER: {}", System.getenv().getOrDefault("DB_USER", "root (기본값)"));
    }

    /**
     * 5.3절 - List와 Map 구조 설정 출력
     */
    private void printCollectionProperties() {
        if (appProperties.getServers() != null) {
            log.info("📋 서버 목록 (List 타입):");
            for (int i = 0; i < appProperties.getServers().size(); i++) {
                log.info("   - 서버 {}: {}", i + 1, appProperties.getServers().get(i));
            }
        }
        
        if (appProperties.getErrorCodes() != null) {
            log.info("📋 에러 코드 매핑 (Map 타입):");
            appProperties.getErrorCodes().forEach((key, value) -> 
                log.info("   - {}: {}", key, value)
            );
        }
    }

    /**
     * API 키 마스킹 (보안)
     */
    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() < 4) {
            return "****";
        }
        return apiKey.substring(0, 4) + "*".repeat(apiKey.length() - 4);
    }

    private void printSeparator(String title) {
        log.info("");
        log.info("=" + "=".repeat(50));
        log.info("  {}", title);
        log.info("=" + "=".repeat(50));
    }
} 