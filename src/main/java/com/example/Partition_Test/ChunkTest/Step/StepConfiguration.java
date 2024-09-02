package com.example.Partition_Test.ChunkTest.Step;

import com.example.Partition_Test.ChunkTest.AgvParameter;
import com.example.Partition_Test.ChunkTest.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.entity.first.Agv;
import com.example.Partition_Test.ChunkTest.repository.first.AgvRepository;
import com.example.Partition_Test.ChunkTest.repository.second.MultiDbRepository;
import com.example.Partition_Test.ChunkTest.writer.CustomItemWriter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final AgvParameter agvParameter;
    private final DelegateTest delegateTest;

/*    @Qualifier("firstDataSource")
    private final DataSource firstDataSource;*/


    @Autowired
    @Qualifier("secondDataSource")
    private final DataSource secondDataSource;


    private final AgvRepository agvRepository;
    private final MultiDbRepository multiDbRepository;


    @Bean
    @JobScope
    public Step step(JobRepository jobRepository
             ,PlatformTransactionManager platformTransactionManager) throws Exception {

       // System.out.println("firstDataSource : " + firstDataSource);
        System.out.println("secondDataSource : " + secondDataSource);

        return new StepBuilder("step", jobRepository)
                .<Agv, Agv>chunk(agvParameter.getChunkSize(), platformTransactionManager)
                .reader(agvItemReader())
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step step2(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("step2", jobRepository)
                .<Agv, Agv>chunk(agvParameter.getChunkSize(), platformTransactionManager)
                .reader(agvItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step step3(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("step3", jobRepository)
                .<Agv, Agv>chunk(agvParameter.getChunkSize(), platformTransactionManager)
                .reader(agvItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Agv> agvItemReader() throws Exception {

        JdbcPagingItemReader<Agv> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(agvParameter.getDataSource());
        reader.setPageSize(agvParameter.getChunkSize());
        reader.setRowMapper(new BeanPropertyRowMapper(Agv.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, time, even");
        queryProvider.setFromClause("from agv");

        HashMap<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }


    @Bean
    @StepScope
    public CustomItemWriter<Agv> customItemWriter(){

        String sql = "insert into agvsum (sum, agv_id) values (:sum, :agvId)";

        CustomItemWriter<Agv> agvCustomItemWriter = new CustomItemWriter<>(DelegateEnum.EVEN_ODD,true, agvRepository, multiDbRepository);

        agvCustomItemWriter.setDelegateTest(delegateTest);
        agvCustomItemWriter.setDataSource(agvParameter.getDataSource());
        agvCustomItemWriter.setSql(sql);
        agvCustomItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        agvCustomItemWriter.afterPropertiesSet();

        return agvCustomItemWriter;
    }

    @Bean
    @StepScope
    public CustomItemWriter<Agv> customItemWriter2(){

        String sql = "insert into agvsum (sum, agv_id) values (:sum, :agvId)";

        CustomItemWriter<Agv> agvCustomItemWriter = new CustomItemWriter<>(DelegateEnum.EVEN_ODD, false, agvRepository, multiDbRepository);

        agvCustomItemWriter.setDelegateTest(delegateTest);


        agvCustomItemWriter.setDataSource(secondDataSource);
        agvCustomItemWriter.setSql(sql);
        agvCustomItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        agvCustomItemWriter.afterPropertiesSet();

        return agvCustomItemWriter;
    }

/*
    @Bean
    @StepScope
    public ItemWriter<Agv> jpaItemWriter(){
        return new JpaItemWriterBuilder<Agv>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
*/



    @Bean
    @StepScope
    public CompositeItemWriter<Agv> compositeItemWriter(){
        List<ItemWriter<? super Agv>> writers = Stream.of(
                customItemWriter(),
                customItemWriter2()
        ).collect(Collectors.toList());

        return new CompositeItemWriterBuilder<Agv>()
                .delegates(writers)
                .build();
    }
}
