package com.example.Partition_Test.ChunkTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgvAgvSumDto {

    private int time;
    private int sum;
}
