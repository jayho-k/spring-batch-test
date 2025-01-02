package com.example.Partition_Test.ChunkTest.config.paramter;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.Partition_Test.ChunkTest.config.paramter.ParameterConfig.DB_PATH;

@Getter
@ToString
@ConfigurationProperties(DB_PATH)
public class DbParam {

    private final String MIXING_USERNAME;
    private final String ELECTRODE_USERNAME;
    private final List<String> SCHEMAS;

    public DbParam(String mixingUsername, String electrodeUsername){
        this.MIXING_USERNAME = mixingUsername;
        this.ELECTRODE_USERNAME = electrodeUsername;
        this.SCHEMAS = List.of(mixingUsername, electrodeUsername);
    }
}
