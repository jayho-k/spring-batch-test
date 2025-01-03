package com.example.Partition_Test.ChunkTest.runner;

import com.example.Partition_Test.ChunkTest.config.paramter.DbParam;
import com.example.Partition_Test.ChunkTest.config.paramter.MapDataFactory;
import com.example.Partition_Test.ChunkTest.config.paramter.MapParam;
import com.example.Partition_Test.ChunkTest.config.tenant.TenantIdentifierResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartitionRunner {

    @Qualifier("partitionJob")
    private final Job partitionJob;
    private final JobLauncher jobLauncher;
    private final TenantIdentifierResolver tenantIdentifierResolver;
    private final DbParam dbParam;

    private final MapDataFactory mapDataFactory;

    //private final Mixing mixing;
    //private final Electrode electrode;


    @Scheduled(cron = "0/10 * * * * *")
    public void runPartitionRunner(){

        dbParam.getSCHEMAS().stream()
                .forEach(
                    schema ->{
                        System.out.println(mapDataFactory.getMapData(schema).getPath());

                        //System.out.println(electrode.getPath());

//                        tenantIdentifierResolver.setCurrentTenant(schema);
//                        JobParameters jobParameters = new JobParametersBuilder()
//                                .addString("version", "1.0")
//                                .addString("usage", "parititon")
//                                .addString("schema", schema)
//                                .addLong("timestamp", System.currentTimeMillis())
//                                .toJobParameters();
//
//                        try{
//                            jobLauncher.run(partitionJob, jobParameters);
//                        }
//                        catch (JobExecutionAlreadyRunningException | JobRestartException |
//                               JobInstanceAlreadyCompleteException | JobParametersInvalidException e){
//                            throw new RuntimeException(e);
//                        }
//                        finally {
//                            tenantIdentifierResolver.removeCurrentTenant();
//                        }
                    }
                );
    }
}
