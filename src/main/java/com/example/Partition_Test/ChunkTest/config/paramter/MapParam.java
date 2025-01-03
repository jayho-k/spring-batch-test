package com.example.Partition_Test.ChunkTest.config.paramter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.example.Partition_Test.ChunkTest.config.paramter.ParameterConfig.MAP_PATH;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = MAP_PATH)
public class MapParam {

    private final Mixing mixing;
    private final Electrode electrode;

//    @Getter
//    @Setter
//    @RequiredArgsConstructor
//    public class Mixing implements MapData{
//        private final String path;
//        private String fab;
//    }
//
//    @Getter
//    @Setter
//    @RequiredArgsConstructor
//    public class Electrode implements MapData{
//        private final String path;
//        private String fab;
//    }


}
