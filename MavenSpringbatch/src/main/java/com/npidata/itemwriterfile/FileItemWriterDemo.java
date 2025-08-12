package com.npidata.itemwriterfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileItemWriterDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcReader")
	private ItemReader<? extends Customer> dbjdbcReader;
	
	@Autowired
	@Qualifier("fileItemWriter")
	private ItemWriter<? super Customer> fileItemWriter;
	
	@Bean
	public Job fileItemWriterDemoJob()
	{
		return jobBuilderFactory.get("fileItemWriterDemoJob")
				.start(fileItemWriterDemoStep())
				.build();
	}

	@Bean
	public Step fileItemWriterDemoStep() {
		return stepBuilderFactory.get("fileItemWriterDemoStep")
				.<Customer, Customer>chunk(10)
				.reader(dbjdbcReader)
				.writer(fileItemWriter)
				.build();
	}
}
