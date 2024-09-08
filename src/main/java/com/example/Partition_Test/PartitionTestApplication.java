package com.example.Partition_Test;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Set;

import static org.springframework.boot.WebApplicationType.NONE;

@SpringBootApplication
@RequiredArgsConstructor
public class PartitionTestApplication {

	private final JobRepository jobRepository;
	private final JobExplorer jobExplorer;

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(PartitionTestApplication.class);
		springApplication.setWebApplicationType(NONE);
		springApplication.run(args);
	}

}
