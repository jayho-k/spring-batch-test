package com.example.Partition_Test.ChunkTest.service;

import com.example.Partition_Test.ChunkTest.service.dto.AgvSumDto;
import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import org.springframework.batch.item.Chunk;

public interface EvenOddService {

    Chunk<AgvSumDto> calculate(Chunk<Agv> chunk, boolean isEven);

}
