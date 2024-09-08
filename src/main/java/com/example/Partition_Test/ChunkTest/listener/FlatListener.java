package com.example.Partition_Test.ChunkTest.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@Component
@RequiredArgsConstructor
public class FlatListener implements JobExecutionListener {

    private static int intervalHour = 0;
    private static int intervalMin = 10;

    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        // last execution을 한번에 얻을 수 있는 방법이 없나?
        JobInstance partitionJobInstance = jobExplorer.getLastJobInstance("partitionJob");
        JobExecution lastJobExecution = jobExplorer.getLastJobExecution(partitionJobInstance);
        Set<JobExecution> partitionJobSet = jobExplorer.findRunningJobExecutions("partitionJob");

        // lastJobExecution이 complete여야 map data가 완성된 것
        try{
            // execution 중에 running 있는 경우
            if (partitionJobSet.contains(lastJobExecution)){
                // 개수가 많은 경우 ==> 잘못 꺼져서 많은 경우 삭제를 할 것인가?
                // 작업을 진행하면 안됨
                throw new Exception("this is running");
            }
            // running은 없는데 complete가 안된 경우
            if (lastJobExecution.getStatus() != BatchStatus.COMPLETED) {
                throw new Exception("this is not complete");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        File file = new File("C:/Users/jayho/Developer/flatTest.txt");
        try {

            FileReader reader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(reader);

            String[]set = bufferReader.readLine().split(" ");
            ArrayList<String> lst = new ArrayList<String>(Arrays.asList("flat1", "flat2", "flat3"));

            int i = 0;
            for (String s : set){
                jobExecution.getExecutionContext().put(lst.get(i), Integer.parseInt(s));
                i++;
                System.out.println(s);
            }

            bufferReader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        Duration duration = Duration.between(jobExecution.getEndTime(), jobExecution.getStartTime());

        System.out.println("job execute time : "  + duration);

    }
}
