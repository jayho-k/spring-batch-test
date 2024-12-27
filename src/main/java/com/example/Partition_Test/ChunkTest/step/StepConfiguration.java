package com.example.Partition_Test.ChunkTest.step;

import com.example.Partition_Test.ChunkTest.AgvParameter;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.domain.entity.Agv;

import com.example.Partition_Test.ChunkTest.listener.AgvStepListener;
import com.example.Partition_Test.ChunkTest.domain.repository.AgvRepository;
import com.example.Partition_Test.ChunkTest.step.writer.CustomItemWriter;
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
    private final DelegateTest delegateTest;
    private final DataSource dataSource;
    private final AgvRepository agvRepository;

    @Bean
    @JobScope
    public Step step(JobRepository jobRepository
             ,PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("step", jobRepository)
                .<Agv, Agv>chunk(agvParameter.getChunkSize(), platformTransactionManager)
                .reader(agvItemReader())
                .writer(customItemWriter())
                .listener(new AgvStepListener())
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

        CustomItemWriter<Agv> agvCustomItemWriter = new CustomItemWriter<>(DelegateEnum.EVEN_ODD,true, agvRepository);

        agvCustomItemWriter.setDelegateTest(delegateTest);
        agvCustomItemWriter.setDataSource(dataSource);
        agvCustomItemWriter.setSql(sql);
        agvCustomItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        agvCustomItemWriter.afterPropertiesSet();

        return agvCustomItemWriter;
    }
}
