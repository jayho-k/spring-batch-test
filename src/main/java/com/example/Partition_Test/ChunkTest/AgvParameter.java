package com.example.Partition_Test.ChunkTest;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Getter
@Component
@RequiredArgsConstructor
public class AgvParameter {

    private final DataSource dataSource;
    private int chunkSize = 30;
}
