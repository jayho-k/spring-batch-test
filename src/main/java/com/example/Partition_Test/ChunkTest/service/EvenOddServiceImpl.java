package com.example.Partition_Test.ChunkTest.service;

import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import com.example.Partition_Test.ChunkTest.domain.repository.AddTableRepository;
import com.example.Partition_Test.ChunkTest.service.dto.AgvSumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EvenOddServiceImpl implements EvenOddService{

    private final AddTableRepository addTableRepository;

    @Override
    public Chunk<AgvSumDto> calculate(Chunk<Agv> chunk, boolean isEven) {

        Chunk<AgvSumDto> agvSumChunk = new Chunk<>();

        List<Long> collect = addTableRepository.findAll().stream().map(ad -> ad.getId()).collect(Collectors.toList());

        int addTableId = -1;
        if (collect.size()!=0){
            addTableId = collect.get(0).intValue();
        }

        int sum = 0;
        if (isEven){
            for (Agv agv : chunk.getItems()){
                sum ++;
                if (agv.getTime() %2 == 0){
                    agvSumChunk.add(agvSumBuild(sum, addTableId, agv.getId()));
                    System.out.println("is even : " + agv.getTime());
                }
            }
        }

        else{
            for (Agv agv : chunk.getItems()){
                sum ++;
                if (agv.getTime() %2 == 1){
                    agvSumChunk.add(agvSumBuild(sum, addTableId, agv.getId()));
                    System.out.println("is not even : " + agv.getTime());
                }
            }
        }
        return agvSumChunk;
    }

    private AgvSumDto agvSumBuild(int sum, int addTableId, int agvId){
        return new AgvSumDto().builder()
                .sum(sum + addTableId)
                .agvId(agvId)
                .build();
    }


}
