package com.example.Partition_Test.ChunkTest.Step;

import com.example.Partition_Test.ChunkTest.entity.first.Flat1;
import com.example.Partition_Test.ChunkTest.mapper.flat1Mapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FlatStepConfig {

    private int chunkSize = 11;

    private final EntityManagerFactory entityManagerFactory;


    @Bean
    @JobScope
    public Step flatStep1(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("flatStep1", jobRepository)
                .<Flat1, Flat1>chunk(chunkSize, platformTransactionManager)
                .reader(flat1ItemReader())
                .writer(flat1ItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader flat1ItemReader(){
        FlatFileItemReader<Flat1> reader = new FlatFileItemReader<>();

        reader.setResource(new PathResource("C:/Users/jayho/Developer/flatTest.txt"));
        DefaultLineMapper<Flat1> lineMapper = new DefaultLineMapper<>();

        lineMapper.setLineTokenizer(new DelimitedLineTokenizer(" "));
        lineMapper.setFieldSetMapper(new flat1Mapper());

        reader.setLineMapper(lineMapper);
        reader.setLinesToSkip(1);
        reader.setMaxItemCount(10);

        return reader;
    }


    @Bean
    public ItemWriter<Flat1> flat1ItemWriter(){
        return new JpaItemWriterBuilder<Flat1>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }



}




















