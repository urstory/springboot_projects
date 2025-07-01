package com.example.springprofiledemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    private String name;
    private String environment;
    private String description;
    private String version;
    private DatabaseConfig database;
    private FeatureConfig features;
    
    @Data
    public static class DatabaseConfig {
        private String url;
        private String username;
        private String password;
        private int maxConnections;
    }
    
    @Data
    public static class FeatureConfig {
        private boolean debugMode;
        private boolean enableCache;
        private boolean enableMetrics;
        private int timeout;
    }
} 