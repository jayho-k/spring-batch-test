package com.example.Partition_Test.ChunkTest.service;

import com.example.Partition_Test.ChunkTest.dto.AgvSumDto;
import com.example.Partition_Test.ChunkTest.entity.first.Agv;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Service;

@Service
public class EvenOddServiceImpl implements EvenOddService{

    @Override
    public Chunk<AgvSumDto> calculate(Chunk<Agv> chunk, boolean isEven) {

        Chunk<AgvSumDto> agvSumChunk = new Chunk<>();

        int sum = 0;
        if (isEven){
            for (Agv agv : chunk.getItems()){

                sum ++;
                if (agv.getTime() %2 == 0){
                    AgvSumDto agvSumDto = new AgvSumDto();
                    agvSumDto.setSum(sum);
                    agvSumDto.setAgvId(agv.getId());
                    agvSumChunk.add(agvSumDto);
                    System.out.println("is even : " + agv.getTime());
                }
            }
        }
        else{
            for (Agv agv : chunk.getItems()){

                sum ++;
                if (agv.getTime() %2 == 1){
                    AgvSumDto agvSumDto = new AgvSumDto();
                    agvSumDto.setSum(sum);
                    agvSumDto.setAgvId(agv.getId());
                    agvSumChunk.add(agvSumDto);
                    System.out.println("is not even : " + agv.getTime());
                }
            }
        }

        return agvSumChunk;
    }
}
