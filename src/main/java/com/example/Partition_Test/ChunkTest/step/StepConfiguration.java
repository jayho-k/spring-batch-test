package com.example.Partition_Test.ChunkTest.step;

import com.example.Partition_Test.ChunkTest.AgvParameter;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.domain.entity.Agv;

import com.example.Partition_Test.ChunkTest.listener.AgvStepListener;
import com.example.Partition_Test.ChunkTest.domain.repository.AgvRepository;
import com.example.Partition_Test.ChunkTest.step.writer.CustomItemWriter;
import com.example.Partition_Test.ChunkTest.tasklet.chunk.AgvTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;



@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final AgvParameter agvParameter;
    private final AgvTasklet agvTasklet;

    @Bean
    @JobScope
    public Step step(JobRepository jobRepository
             ,PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("step", jobRepository)
                .<Agv, Agv>chunk(agvParameter.getChunkSize(), platformTransactionManager)
                .reader(agvTasklet.agvItemReader(null))
                .writer(agvTasklet.customItemWriter())
                .listener(new AgvStepListener())
                .build();
    }


}
