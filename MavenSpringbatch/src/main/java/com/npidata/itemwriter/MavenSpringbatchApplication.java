package com.npidata.itemwriter;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class MavenSpringbatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MavenSpringbatchApplication.class, args);
	}

}
