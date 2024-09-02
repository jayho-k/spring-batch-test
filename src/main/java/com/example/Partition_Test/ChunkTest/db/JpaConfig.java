package com.example.Partition_Test.ChunkTest.db;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
public class JpaConfig {


    private static final String FIRST_ENTITY_MANAGER_FACTORY = "firstEntityManagerFactory";
    private static final String SECOND_ENTITY_MANAGER_FACTORY = "secondEntityManagerFactory";
    private static final String FIRST_TRANSACTION_MANAGER = "firstTransactionManager";
    private static final String SECOND_TRANSACTION_MANAGER = "secondTransactionManager";

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    private final String FIRST_DOMAIN_PACKAGE = "com.example.Partition_Test.ChunkTest.entity.first";
    private final String SECOND_DOMAIN_PACKAGE = "com.example.Partition_Test.ChunkTest.entity.second";

    /**
     * spring data jpa 의존성이있으면 EntityManagerFactory 를 자동으로 빈으로 설정할 수 있음.
     * 단, @ConditionalOnMissingBean 이 걸려있기에 entityManagerFactory 로 등록된 bean 이 없을 때만
     * 빈으로 설정 됨.
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(BatchDataSourceConfig.FIRST_DATASOURCE) DataSource dataSource)  {
        return EntityManagerFactoryCreator.builder()
                .properties(jpaProperties)
                .hibernateProperties(hibernateProperties)
                .metadataProviders(metadataProviders)
                .entityManagerFactoryBuilder(entityManagerFactoryBuilder)
                .dataSource(dataSource)
                .packages(FIRST_DOMAIN_PACKAGE)
                .persistenceUnit("mainUnit")
                .build()
                .create();
    }

    @Bean(name = SECOND_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier(BatchDataSourceConfig.SECOND_DATASOURCE) DataSource dataSource)  {
        return EntityManagerFactoryCreator.builder()
                .properties(jpaProperties)
                .hibernateProperties(hibernateProperties)
                .metadataProviders(metadataProviders)
                .entityManagerFactoryBuilder(entityManagerFactoryBuilder)
                .dataSource(dataSource)
                .packages(SECOND_DOMAIN_PACKAGE)
                .persistenceUnit("subUnit")
                .build()
                .create();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }

    @Bean(name = SECOND_TRANSACTION_MANAGER)
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier(SECOND_ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }



    public static final String FIRST_REPOSITORY_PACKAGE = "com.example.Partition_Test.ChunkTest.repository.first";
    public static final String SECOND_REPOSITORY_PACKAGE = "com.example.Partition_Test.ChunkTest.repository.second";

    @Configuration
    @EnableJpaRepositories(
            basePackages = FIRST_REPOSITORY_PACKAGE
            ,entityManagerFactoryRef = "entityManagerFactory"
            ,transactionManagerRef = JpaConfig.FIRST_TRANSACTION_MANAGER
    )
    public static class FirstJpaRepositoriesConfig{}

    @Configuration
    @EnableJpaRepositories(
            basePackages = SECOND_REPOSITORY_PACKAGE
            ,entityManagerFactoryRef = JpaConfig.SECOND_ENTITY_MANAGER_FACTORY
            ,transactionManagerRef = JpaConfig.SECOND_TRANSACTION_MANAGER
    )
    public static class SecondJpaRepositoriesConfig{}



/*    // batchDatasource 사용을 위한 수동 빈 등록
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
                                                                     JobRepository jobRepository, BatchProperties properties) {
        JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobNames = properties.getJob().getName();
        if (StringUtils.hasText(jobNames)) {
            runner.setJobName(jobNames);
        }
        return runner;
    }

    // batchDatasource 사용을 위한 수동 빈 등록
    @Bean
    @ConditionalOnMissingBean(BatchDataSourceScriptDatabaseInitializer.class)
    BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(DataSource dataSource,
                                                                        @BatchDataSource ObjectProvider<DataSource> batchDataSource, BatchProperties properties) {
        return new BatchDataSourceScriptDatabaseInitializer(batchDataSource.getIfAvailable(() -> dataSource),
                properties.getJdbc());
    }*/

}
