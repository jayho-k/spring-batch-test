package com.example.Partition_Test.ChunkTest.Step;

import com.example.Partition_Test.ChunkTest.AgvParameter;
import com.example.Partition_Test.ChunkTest.delegate.DelegateEnum;
import com.example.Partition_Test.ChunkTest.delegate.DelegateTest;
import com.example.Partition_Test.ChunkTest.entity.first.Agv;

import com.example.Partition_Test.ChunkTest.listener.AgvStepListener;
import com.example.Partition_Test.ChunkTest.repository.AgvRepositoryFactory;
import com.example.Partition_Test.ChunkTest.repository.first.AgvRepository1;
import com.example.Partition_Test.ChunkTest.repository.second.AgvRepository2;
import com.example.Partition_Test.ChunkTest.repository.second.MultiDbRepository;
import com.example.Partition_Test.ChunkTest.writer.CustomItemWriter;
import jakarta.persistence.EntityManagerFactory;
import lombok.Data;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final AgvParameter agvParameter;
    private final DelegateTest delegateTest;

    @Autowired
    @Qualifier("firstDataSource")
    private final DataSource firstDataSource;

    @Autowired
    @Qualifier("secondDataSource")
    private final DataSource secondDataSource;

    private final AgvRepositoryFactory agvRepositoryFactory;

    @Bean
    @JobScope
    public Step step(JobRepository jobRepository
             ,PlatformTransactionManager platformTransactionManager
    , @Value("#{jobExecutionContext['dataSourceMap']}") Map<String, Boolean> dataSourceMap) throws Exception {

        System.out.println("firstDataSource : " + firstDataSource);
        System.out.println("secondDataSource : " + secondDataSource);
        System.out.println("dataSourceMap : " + dataSourceMap);

        return new StepBuilder("step", jobRepository)
                .<Agv, Agv>chunk(agvParameter.getChunkSize(), platformTransactionManager)
                .reader(agvItemReader(dataSourceMap))
                .writer(customItemWriter(dataSourceMap))
                .listener(new AgvStepListener())
                .build();
    }

/*    @Bean
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
    }*/

    @Bean
    @StepScope
    public ItemReader<Agv> agvItemReader(@Value("#{jobExecutionContext['dataSourceMap']}") Map<String, Boolean> dataSourceMap) throws Exception {

        JdbcPagingItemReader<Agv> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(agvParameter.getDataSource());
        reader.setPageSize(agvParameter.getChunkSize());
        reader.setRowMapper(new BeanPropertyRowMapper(Agv.class));

        reader.setDataSource(getDataSource(dataSourceMap));

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
    public CustomItemWriter<Agv> customItemWriter(@Value("#{jobExecutionContext['dataSourceMap']}") Map<String, Boolean> dataSourceMap){

        String sql = "insert into agvsum (sum, agv_id) values (:sum, :agvId)";

        CustomItemWriter<Agv> agvCustomItemWriter = new CustomItemWriter<>(DelegateEnum.EVEN_ODD,true, dataSourceMap, agvRepositoryFactory);

        agvCustomItemWriter.setDelegateTest(delegateTest);
        agvCustomItemWriter.setDataSource(getDataSource(dataSourceMap));
        agvCustomItemWriter.setSql(sql);
        agvCustomItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        agvCustomItemWriter.afterPropertiesSet();

        return agvCustomItemWriter;
    }

//    @Bean
//    @StepScope
//    public CustomItemWriter<Agv> customItemWriter2(){
//
//        String sql = "insert into agvsum (sum, agv_id) values (:sum, :agvId)";
//
//        CustomItemWriter<Agv> agvCustomItemWriter = new CustomItemWriter<>(DelegateEnum.EVEN_ODD, false, agvRepository, multiDbRepository);
//
//        agvCustomItemWriter.setDelegateTest(delegateTest);
//
//
//        agvCustomItemWriter.setDataSource(secondDataSource);
//        agvCustomItemWriter.setSql(sql);
//        agvCustomItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//
//        agvCustomItemWriter.afterPropertiesSet();
//
//        return agvCustomItemWriter;
//    }

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

    private DataSource getDataSource(Map<String, Boolean>dataSourceMap){
        for (String dataSourceMapKey : dataSourceMap.keySet()){
            if (dataSourceMap.get(dataSourceMapKey) == true) continue;
            if (dataSourceMapKey.equals("1")){
                System.out.println("datasource1 : " + firstDataSource);
                return firstDataSource;
            }
            else if (dataSourceMapKey.equals("2")){
                System.out.println("datasource2 : "+ secondDataSource);
                return secondDataSource;
            }
        }
        return null;
    }



//    @Bean
//    @StepScope
//    public CompositeItemWriter<Agv> compositeItemWriter(){
//        List<ItemWriter<? super Agv>> writers = Stream.of(
//                customItemWriter(),
//                customItemWriter2()
//        ).collect(Collectors.toList());
//
//        return new CompositeItemWriterBuilder<Agv>()
//                .delegates(writers)
//                .build();
//    }
}
