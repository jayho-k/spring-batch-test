package com.example.Partition_Test.ChunkTest.tasklet.chunk;

import com.example.Partition_Test.ChunkTest.AgvParameter;
import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import com.example.Partition_Test.ChunkTest.domain.repository.AgvRepository;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.service.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.step.writer.CustomItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;

@JobScope
@Configuration
@RequiredArgsConstructor
public class AgvTaskletImpl implements AgvTasklet{
    private final AgvParameter agvParameter;
    private final DelegateTest delegateTest;
    private final DataSource dataSource;
    private final AgvRepository agvRepository;
    @Value("#{jobExecutionContext[agvJobType]}")
    private String agvJobType;

    @Bean
    @StepScope
    public JdbcPagingItemReader<Agv> agvItemReader(@Value("#{stepExecutionContext[test]}") String test) throws Exception {

        System.out.println(agvJobType);
        System.out.println(test);

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

        //System.out.println(agvJobType);

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
