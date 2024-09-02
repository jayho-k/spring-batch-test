package com.example.Partition_Test.ChunkTest.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class AgvJobListener implements JobExecutionListener {

    private static int intervalHour = 0;
    private static int intervalMin = 10;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        Map<String, LocalDateTime> timeMap = new HashMap<>();
        LocalDateTime standardTime = jobExecution.getStartTime();

        LocalDateTime startTime = standardTime
                .minusHours(intervalHour)
                .minusMinutes(intervalMin);

        LocalDateTime endTime = standardTime;

        System.out.println("Job startTime : " + startTime);
        System.out.println("Job endTime : " + endTime);

        timeMap.put("startTime",startTime);
        timeMap.put("endTime",endTime);

        jobExecution.getExecutionContext().put("agvTimeMap", timeMap);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        Duration duration = Duration.between(jobExecution.getEndTime(), jobExecution.getStartTime());

        System.out.println("job execute time : "  + duration);

    }
}
