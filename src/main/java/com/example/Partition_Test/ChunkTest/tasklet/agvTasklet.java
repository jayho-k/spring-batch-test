package com.example.Partition_Test.ChunkTest.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class agvTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Map<String, LocalDateTime> agvTimeMap = (Map<String, LocalDateTime>)contribution.getStepExecution().getJobExecution().getExecutionContext().get("agvTimeMap");
        agvTimeMap.forEach((key, value) ->
                System.out.println("key : " + key + " value : " + value)
        );

        return RepeatStatus.FINISHED;
    }
}
