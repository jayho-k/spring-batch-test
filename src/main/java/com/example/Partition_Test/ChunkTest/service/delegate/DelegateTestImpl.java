package com.example.Partition_Test.ChunkTest.service.delegate;

import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import com.example.Partition_Test.ChunkTest.service.EvenOddService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelegateTestImpl<T> implements DelegateTest<T>{

    private final EvenOddService evenOddService;

    @Override
    public Chunk<T> calculate(Chunk<T> chunk, DelegateEnum delegateEnum, boolean param) {
        return delegate(delegateEnum, chunk,param);

    }

    private Chunk delegate(DelegateEnum delegateEnum, Chunk<T> chunk, boolean param){

        switch (delegateEnum){
            case EVEN_ODD:
                Chunk<Agv>agvChunk =(Chunk<Agv>)chunk;
                return evenOddService.calculate(agvChunk,param);
        }

        return chunk;
    }

}
