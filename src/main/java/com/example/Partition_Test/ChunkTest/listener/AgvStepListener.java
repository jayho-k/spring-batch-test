package com.example.Partition_Test.ChunkTest.listener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;



public class AgvStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        stepExecution.getExecutionContext().put("test","test");
    }
}
