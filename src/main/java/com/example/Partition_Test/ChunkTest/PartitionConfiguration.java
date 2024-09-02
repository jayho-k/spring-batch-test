package com.example.Partition_Test.ChunkTest;


import com.example.Partition_Test.ChunkTest.config.MapConfig;
import com.example.Partition_Test.ChunkTest.config.PathConfig;
import com.example.Partition_Test.ChunkTest.listener.AgvJobListener;
import com.example.Partition_Test.ChunkTest.listener.FlatListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PartitionConfiguration {

    private final PathConfig pathConfig;
    private final MapConfig mapConfig;



    @Bean
    public Job partitionJob(JobRepository jobRepository
                                , @Qualifier("step") Step step){
        System.out.println(pathConfig.mapConfig().getMapPath());

        System.out.println("mapconfig : "+mapConfig.getMapPath());
        return new JobBuilder("partitionJob", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .listener(new AgvJobListener())
                .build();
    }

/*    @Bean
    public Job faltJob(JobRepository jobRepository
            , @Qualifier("flatStep1") Step flatStep1){
        return new JobBuilder("flatJob", jobRepository)
                .start(flatStep1)
                .incrementer(new RunIdIncrementer())
                .listener(new FlatListener())
                .build();
    }*/




}
