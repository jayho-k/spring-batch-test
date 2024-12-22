package com.example.Partition_Test.ChunkTest.writer;


import com.example.Partition_Test.ChunkTest.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.dto.AgvAgvSumDto;
import com.example.Partition_Test.ChunkTest.dto.AgvSumDto;
import com.example.Partition_Test.ChunkTest.entity.first.Agv;
import com.example.Partition_Test.ChunkTest.entity.second.MultiDb;
import com.example.Partition_Test.ChunkTest.repository.first.AgvRepository1;
import com.example.Partition_Test.ChunkTest.repository.second.AgvRepository2;
import com.example.Partition_Test.ChunkTest.repository.second.MultiDbRepository;
import com.example.Partition_Test.ChunkTest.service.EvenOddService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;


import org.springframework.batch.item.database.JdbcBatchItemWriter;


import javax.sql.DataSource;
import java.util.*;


@RequiredArgsConstructor
public class CustomItemWriter<T> extends JdbcBatchItemWriter<T> {

    private final DelegateEnum delegateEnum;
    private final boolean isEven;

    private final AgvRepository1 agvRepository1;
    private final AgvRepository2 agvRepository2;
    private final Map<String, Boolean> dataSourceMap;
    private DelegateTest delegateTest;


    Queue<Agv> q = new LinkedList<>();

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {

        Chunk<AgvSumDto> agvSumChunk;

        List<Integer> times = new ArrayList<>();

        times.add(1);
        times.add(2);
        times.add(4);
        times.add(7);
        times.add(8);
        times.add(9);

        List<AgvAgvSumDto> agvAndSum = getAgvAndSum(times);

        for(AgvAgvSumDto agv : agvAndSum){
            System.out.println("agv time : " + agv.getTime());
        }

//        MultiDb multiDb = new MultiDb();
//        multiDb.setMultidb(3);
//        multiDbRepository.save(multiDb);


        agvSumChunk = delegateTest.calculate(chunk,delegateEnum, isEven);

        System.out.println(q.size());

        super.write((Chunk<? extends T>) agvSumChunk);
    }

    public void setDelegateTest(DelegateTest delegateTest){
        this.delegateTest = delegateTest;
    }


    private List<AgvAgvSumDto> getAgvAndSum(List<Integer> times){

        for (String dataSourceMapKey : dataSourceMap.keySet()){
            if (dataSourceMap.get(dataSourceMapKey) == true) continue;
            if (dataSourceMapKey.equals("1")){
                return agvRepository1.findAgvAndSum(times);
            }
            else if (dataSourceMapKey.equals("2")){
                return agvRepository2.findAgvAndSum(times);
            }
        }
        return null;
    }

}
