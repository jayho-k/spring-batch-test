package com.example.Partition_Test.ChunkTest.listener;

import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;



public class AgvStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {

        String stepName = stepExecution.getStepName();

        Map<String, Boolean> dataSourceMap = (Map<String, Boolean>) stepExecution.getJobExecution().getExecutionContext().get("dataSourceMap");

        stepName += getIngDataSource(dataSourceMap);

        //stepExecution.getExecutionContext().put("name",stepName);

        System.out.println("stepName : " + stepName);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        Map<String, Boolean> dataSourceMap = (Map<String, Boolean>) executionContext.get("dataSourceMap");
        Map<String, Boolean> dataSourceMapUpdated = updateIngDataSource(dataSourceMap);
        executionContext.put("dataSourceMap", dataSourceMapUpdated);

        System.out.println("finish step : " + stepExecution.getExecutionContext().get("name"));
        System.out.println("Map : " + stepExecution.getJobExecution().getExecutionContext().get("datasourceMap"));

        return ExitStatus.COMPLETED;
    }

    private String getIngDataSource(Map<String, Boolean> dataSourceMap){

        for (String ds : dataSourceMap.keySet()){
            if (dataSourceMap.get(ds) == false){
                return ds;
            }
        }
        return null;
    }

    private Map<String, Boolean> updateIngDataSource(Map<String, Boolean> dataSourceMap){

        for (String ds : dataSourceMap.keySet()){
            if (dataSourceMap.get(ds) == false){
                dataSourceMap.put(ds,true);
                return dataSourceMap;
            }
        }

        return dataSourceMap;
    }

}
