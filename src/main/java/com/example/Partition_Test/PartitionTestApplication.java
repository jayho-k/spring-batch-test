package com.example.Partition_Test;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;


import static org.springframework.boot.WebApplicationType.NONE;

@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class PartitionTestApplication {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(PartitionTestApplication.class);
		springApplication.setWebApplicationType(NONE);
		springApplication.run(args);
	}

}
