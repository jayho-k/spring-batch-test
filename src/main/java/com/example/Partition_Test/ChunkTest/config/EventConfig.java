package com.example.Partition_Test.ChunkTest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class EventConfig {

    private final JobRepository jobRepository;
    private final JobExplorer jobExplorer;

    @EventListener(ApplicationStartedEvent.class)
    public void init(){

        System.out.println("Listener start!!");

        List<String> jobNames = jobExplorer.getJobNames();
        for(String jobName : jobNames){
            Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(jobName);
            runningJobExecutions.stream().forEach(
                    runningJE -> jobRepository.deleteJobExecution(runningJE)
            );
        }
    }
}
