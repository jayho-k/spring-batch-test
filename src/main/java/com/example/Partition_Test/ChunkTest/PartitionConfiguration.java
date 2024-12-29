package com.example.Partition_Test.ChunkTest;


import com.example.Partition_Test.ChunkTest.config.MapConfig;
import com.example.Partition_Test.ChunkTest.config.PathConfig;
import com.example.Partition_Test.ChunkTest.listener.AgvJobListener;
import com.example.Partition_Test.ChunkTest.listener.FlatListener;
import jakarta.el.CompositeELResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
    private final FlatListener flatListener;
    private CompositeELResolver stepExecutionListeners;

    @Bean
    public Job partitionJob(JobRepository jobRepository
                                , @Qualifier("step") Step step){
        return new JobBuilder("partitionJob", jobRepository)
                .start(step)
                .listener(new AgvJobListener("partitionJob"))
                .incrementer(new RunIdIncrementer())
                .build();
    }

//    @Bean
//    public Job partitionJob2(JobRepository jobRepository
//            , @Qualifier("step") Step step){
//        return new JobBuilder("partitionJob2", jobRepository)
//                .start(step)
//                .listener(new AgvJobListener("partitionJob2"))
//                .incrementer(new RunIdIncrementer())
//                .build();
//    }

}
