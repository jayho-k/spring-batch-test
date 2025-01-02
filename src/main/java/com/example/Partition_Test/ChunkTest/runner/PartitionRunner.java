package com.example.Partition_Test.ChunkTest.runner;

import com.example.Partition_Test.ChunkTest.config.paramter.DbParam;
import com.example.Partition_Test.ChunkTest.config.paramter.MapParam;
import com.example.Partition_Test.ChunkTest.config.tenant.TenantIdentifierResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartitionRunner {

    @Qualifier("partitionJob")
    private final Job partitionJob;
    private final JobLauncher jobLauncher;
    private final TenantIdentifierResolver tenantIdentifierResolver;
    private final DbParam dbParam;

    @Scheduled(cron = "0/10 * * * * *")
    public void runPartitionRunner(){

        dbParam.getSCHEMAS().stream()
                .forEach(
                    schema ->{
                        tenantIdentifierResolver.setCurrentTenant(schema);
                        JobParameters jobParameters = new JobParametersBuilder()
                                .addString("version", "1.0")
                                .addString("usage", "parititon")
                                .addString("schema", schema)
                                .addLong("timestamp", System.currentTimeMillis())
                                .toJobParameters();

                        try{
                            jobLauncher.run(partitionJob, jobParameters);
                        }
                        catch (JobExecutionAlreadyRunningException | JobRestartException |
                               JobInstanceAlreadyCompleteException | JobParametersInvalidException e){
                            throw new RuntimeException(e);
                        }
                        finally {
                            tenantIdentifierResolver.removeCurrentTenant();
                        }
                    }
                );
    }
}
