package com.npidata.restart;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class HahaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HahaApplication.class, args);
	}

}
