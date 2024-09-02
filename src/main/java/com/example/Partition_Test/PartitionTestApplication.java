package com.example.Partition_Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static org.springframework.boot.WebApplicationType.NONE;

		@SpringBootApplication
		public class PartitionTestApplication {

			public static void main(String[] args) {

				SpringApplication springApplication = new SpringApplication(PartitionTestApplication.class);
				springApplication.setWebApplicationType(NONE);
				springApplication.run(args);
	}

}
