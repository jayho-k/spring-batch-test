package com.example.Partition_Test.ChunkTest.step.writer;


import com.example.Partition_Test.ChunkTest.service.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.service.dto.AgvAgvSumDto;
import com.example.Partition_Test.ChunkTest.service.dto.AgvSumDto;
import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import com.example.Partition_Test.ChunkTest.domain.repository.AgvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;


import org.springframework.batch.item.database.JdbcBatchItemWriter;


import java.util.*;



@RequiredArgsConstructor
public class CustomItemWriter<T> extends JdbcBatchItemWriter<T> {

    private final DelegateEnum delegateEnum;
    private final boolean isEven;
    private DelegateTest delegateTest;
    private final AgvRepository agvRepository;


    Queue<Agv> q = new LinkedList<>();

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {

        Chunk<AgvSumDto> agvSumChunk;

        agvSumChunk = delegateTest.calculate(chunk, delegateEnum, isEven);

        System.out.println(q.size());

        super.write((Chunk<? extends T>) agvSumChunk);
    }

    public void setDelegateTest(DelegateTest delegateTest){
        this.delegateTest = delegateTest;
    }


}
