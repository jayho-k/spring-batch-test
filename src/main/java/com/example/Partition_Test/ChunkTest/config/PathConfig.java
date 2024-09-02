package com.example.Partition_Test.ChunkTest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathConfig {
    private final String DEFAULT_CLASS = "spring.";
    private final String MAP_PATH = DEFAULT_CLASS + "map";

    @Bean
    @ConfigurationProperties(prefix = MAP_PATH)
    public MapConfig mapConfig(){
        return new MapConfig();

    }
}
