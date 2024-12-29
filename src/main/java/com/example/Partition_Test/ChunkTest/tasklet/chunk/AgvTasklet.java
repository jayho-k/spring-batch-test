package com.example.Partition_Test.ChunkTest.tasklet.chunk;

import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import com.example.Partition_Test.ChunkTest.step.writer.CustomItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;

public interface AgvTasklet{
    JdbcPagingItemReader<Agv> agvItemReader(String test) throws Exception;
    CustomItemWriter<Agv> customItemWriter();
}
