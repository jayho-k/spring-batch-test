package com.example.Partition_Test.ChunkTest.repository;

import com.example.Partition_Test.ChunkTest.repository.first.AgvRepository1;
import com.example.Partition_Test.ChunkTest.repository.second.AgvRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AgvRepositoryFactory {

    private final AgvRepository1 agvRepository1;
    private final AgvRepository2 agvRepository2;

    public AgvRepository getAgvRepository(int type){
        if (type == 1){
            return agvRepository1;
        }
        else{
            return agvRepository2;
        }
    }
}
