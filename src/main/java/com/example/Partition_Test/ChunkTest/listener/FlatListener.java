package com.example.Partition_Test.ChunkTest.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class FlatListener implements JobExecutionListener {

    private static int intervalHour = 0;
    private static int intervalMin = 10;

    @Override
    public void beforeJob(JobExecution jobExecution) {
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
