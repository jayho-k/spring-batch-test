package com.example.Partition_Test.ChunkTest.step.writer;

import com.example.Partition_Test.ChunkTest.domain.entity.Flat1;
import com.example.Partition_Test.ChunkTest.domain.entity.Flat2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;


@RequiredArgsConstructor
public class FlatMultiWrite implements ItemWriter<Flat1> {

    private final JpaItemWriter<Flat1> flat1Writer;

    private final JpaItemWriter<Flat2> flat2Writer;

    @Override
    public void write(Chunk<? extends Flat1> chunk) throws Exception {

        Chunk<Flat1> flat1Chunk = new Chunk<Flat1>();
        Chunk<Flat2> flat2Chunk = new Chunk<Flat2>();

        for (Flat1 flat : chunk){
            if (flat.getFlat1()%2 == 1){
                flat1Chunk.add(Flat1.builder()
                        .flat1(flat.getFlat1())
                        .build());
            }
            else{
                flat2Chunk.add(Flat2.builder()
                        .flat2(flat.getFlat1())
                        .build());
            }
        }

        flat1Writer.write(flat1Chunk);
        flat2Writer.write(flat2Chunk);
    }
}
