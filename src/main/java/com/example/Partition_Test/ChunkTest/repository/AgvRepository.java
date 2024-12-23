package com.example.Partition_Test.ChunkTest.repository;

import com.example.Partition_Test.ChunkTest.dto.AgvAgvSumDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgvRepository {
    List<AgvAgvSumDto> findAgvAndSum(List<Integer> timeList);

}
