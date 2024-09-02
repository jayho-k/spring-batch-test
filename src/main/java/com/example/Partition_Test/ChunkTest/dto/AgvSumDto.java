package com.example.Partition_Test.ChunkTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgvSumDto {

    private int sum;
    private int agvId;

}
