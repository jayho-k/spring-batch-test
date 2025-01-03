package com.example.Partition_Test.ChunkTest.config.paramter;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
