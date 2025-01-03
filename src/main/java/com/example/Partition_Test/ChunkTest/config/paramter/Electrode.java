package com.example.Partition_Test.ChunkTest.config.paramter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Electrode implements MapData{
    private final String path;
    private String fab;
}



