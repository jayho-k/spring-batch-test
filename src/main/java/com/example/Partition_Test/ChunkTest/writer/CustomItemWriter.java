package com.example.Partition_Test.ChunkTest.writer;


import com.example.Partition_Test.ChunkTest.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.dto.AgvAgvSumDto;
import com.example.Partition_Test.ChunkTest.dto.AgvSumDto;
import com.example.Partition_Test.ChunkTest.entity.first.Agv;
import com.example.Partition_Test.ChunkTest.entity.second.MultiDb;
import com.example.Partition_Test.ChunkTest.repository.first.AgvRepository;
import com.example.Partition_Test.ChunkTest.repository.second.MultiDbRepository;
import com.example.Partition_Test.ChunkTest.service.EvenOddService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;


import org.springframework.batch.item.database.JdbcBatchItemWriter;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@RequiredArgsConstructor
public class CustomItemWriter<T> extends JdbcBatchItemWriter<T> {

    private final DelegateEnum delegateEnum;
    private final boolean isEven;
    private final AgvRepository agvRepository;
    private final MultiDbRepository multiDbRepository;
    private DelegateTest delegateTest;

    Queue<Agv> q = new LinkedList<>();

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {

        Chunk<AgvSumDto> agvSumChunk = new Chunk<>();

        List<Integer> times = new ArrayList<>();

        times.add(1);
        times.add(2);
        times.add(4);
        times.add(7);
        times.add(8);
        times.add(9);

        List<AgvAgvSumDto> agvAndSum = agvRepository.findAgvAndSum(times);
        for(AgvAgvSumDto agv : agvAndSum){
            System.out.println("agv time : " + agv.getTime());
        }

        MultiDb multiDb = new MultiDb();
        multiDb.setMultidb(3);
        multiDbRepository.save(multiDb);


        delegateTest.calculate(chunk,delegateEnum, isEven);

        //evenOddService.calculate(AgvChunk, isEven);

        System.out.println(q.size());

        super.write((Chunk<? extends T>) agvSumChunk);
    }

    public void setDelegateTest(DelegateTest delegateTest){
        this.delegateTest = delegateTest;
    }


}
