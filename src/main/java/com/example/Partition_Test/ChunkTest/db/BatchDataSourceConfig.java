package com.example.Partition_Test.ChunkTest.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableJpaRepositories
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class BatchDataSourceConfig {

    private final String DEFAULT_CLASS = "spring.";
    private final String FIRST_PROPERTIES = DEFAULT_CLASS + "datasource-first";
    public static final String FIRST_DATASOURCE = "firstDataSource";
    private final String SECOND_PROPERTIES = DEFAULT_CLASS + "datasource-second";
    public static final String SECOND_DATASOURCE = "secondDataSource";

    @Bean
    @ConfigurationProperties(prefix = FIRST_PROPERTIES)
    public HikariConfig firstHikariConfig() {
        return new HikariConfig();
    }


    /**
     * LazyConnectionDataSourceProxy 로 wrapping 한 이유는 스프링의 경우 트랜잭션 시작 시,
     * datasource 의 connection 를 사용하건 안하건 커넥션을 확보합니다.
     * 그로 인해 불필요한 리소스가 발생하게되고, 이를 줄이기 위해 LazyConnectionDataSourceProxy 로
     * 감쌀 경우 실제 커넥션이 필요한 경우에만 datasource 에서 connection 을 반환합니다.
     * 즉, Multi DataSource 에서 해당 설정을 넣어주면 좋습니다. single datasource 에서는
     * 미리 가져오면 거의 왠만하면 쓰겠지만 2개 이상의 datasource 의 경우는 안쓰는 datasource 가
     * 있을 수 있기에 이 설정을 넣어주면 좋습니다.
     * @return
     */
    @Primary // batch job repository datasource 는 primary 설정 datasource 로 설정됨.
    @Bean(FIRST_DATASOURCE)
    public DataSource firstDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(firstHikariConfig()));
    }

    @Bean
    @ConfigurationProperties(prefix = SECOND_PROPERTIES)
    public HikariConfig secondHikariConfig() {
        return new HikariConfig();
    }

    @Bean(SECOND_DATASOURCE)
    public DataSource secondDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(secondHikariConfig()));
    }
}
