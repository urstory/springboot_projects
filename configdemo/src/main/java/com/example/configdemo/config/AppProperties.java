package com.example.configdemo.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 내용.md 5.3절 예제 - 커스텀 프로퍼티 작성 및 주입하기
 * @ConfigurationProperties 어노테이션을 이용해 YAML 설정을 자바 객체로 자동 매핑
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    private String apiKey;
    private String environment;
    private boolean debugMode;
    private boolean cacheEnabled;
    private Timeout timeout;
    private List<String> servers;
    private Map<String, Integer> errorCodes;
    private Features features;
    private ExternalApis externalApis;
    private Security security;

    /**
     * Timeout 설정을 위한 중첩 클래스
     */
    public static class Timeout {
        private int connection;
        private int read;

        public int getConnection() { return connection; }
        public void setConnection(int connection) { this.connection = connection; }
        public int getRead() { return read; }
        public void setRead(int read) { this.read = read; }

        @Override
        public String toString() {
            return String.format("Timeout{connection=%d, read=%d}", connection, read);
        }
    }

    /**
     * Features 설정을 위한 중첩 클래스
     */
    public static class Features {
        private boolean mockExternalApi;
        private boolean logAllRequests;

        public boolean isMockExternalApi() { return mockExternalApi; }
        public void setMockExternalApi(boolean mockExternalApi) { this.mockExternalApi = mockExternalApi; }
        public boolean isLogAllRequests() { return logAllRequests; }
        public void setLogAllRequests(boolean logAllRequests) { this.logAllRequests = logAllRequests; }

        @Override
        public String toString() {
            return String.format("Features{mockExternalApi=%s, logAllRequests=%s}", 
                               mockExternalApi, logAllRequests);
        }
    }

    /**
     * External APIs 설정을 위한 중첩 클래스
     */
    public static class ExternalApis {
        private int timeout;
        private int retryCount;

        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

        @Override
        public String toString() {
            return String.format("ExternalApis{timeout=%d, retryCount=%d}", timeout, retryCount);
        }
    }

    /**
     * Security 설정을 위한 중첩 클래스
     */
    public static class Security {
        private boolean csrfEnabled;
        private boolean sslRequired;

        public boolean isCsrfEnabled() { return csrfEnabled; }
        public void setCsrfEnabled(boolean csrfEnabled) { this.csrfEnabled = csrfEnabled; }
        public boolean isSslRequired() { return sslRequired; }
        public void setSslRequired(boolean sslRequired) { this.sslRequired = sslRequired; }

        @Override
        public String toString() {
            return String.format("Security{csrfEnabled=%s, sslRequired=%s}", csrfEnabled, sslRequired);
        }
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debugMode) { this.debugMode = debugMode; }

    public boolean isCacheEnabled() { return cacheEnabled; }
    public void setCacheEnabled(boolean cacheEnabled) { this.cacheEnabled = cacheEnabled; }

    public Timeout getTimeout() { return timeout; }
    public void setTimeout(Timeout timeout) { this.timeout = timeout; }

    public List<String> getServers() { return servers; }
    public void setServers(List<String> servers) { this.servers = servers; }

    public Map<String, Integer> getErrorCodes() { return errorCodes; }
    public void setErrorCodes(Map<String, Integer> errorCodes) { this.errorCodes = errorCodes; }

    public Features getFeatures() { return features; }
    public void setFeatures(Features features) { this.features = features; }

    public ExternalApis getExternalApis() { return externalApis; }
    public void setExternalApis(ExternalApis externalApis) { this.externalApis = externalApis; }

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    @Override
    public String toString() {
        return String.format(
            "AppProperties{name='%s', version='%s', environment='%s', debugMode=%s, cacheEnabled=%s, " +
            "timeout=%s, servers=%s, errorCodes=%s, features=%s, externalApis=%s, security=%s}",
            name, version, environment, debugMode, cacheEnabled, 
            timeout, servers, errorCodes, features, externalApis, security
        );
    }
} 