package com.example.Partition_Test.ChunkTest.config.paramter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;




@Slf4j
@Component
@EnableConfigurationProperties({MapParam.class, DbParam.class})
public class ParameterConfig {
    private final static String DEFAULT_CLASS = "spring.";
    public final static String MAP_PATH = DEFAULT_CLASS + "map";
    public final static String DB_PATH = DEFAULT_CLASS + "db-param";

}
