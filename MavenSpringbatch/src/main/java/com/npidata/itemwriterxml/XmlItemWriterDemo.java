package com.npidata.itemwriterxml;

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
public class XmlItemWriterDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcReader")
	private ItemReader<? extends Customer> dbjdbcReader;

	@Autowired
	@Qualifier("xmlItemWriter")
	private ItemWriter<? super Customer> xmlItemWriter;
	
	@Bean
	public Job xmlItemWriterDemoJob()
	{
		return jobBuilderFactory.get("xmlItemWriterDemoJob")
				.start(xmlItemWriterDemoStep())
				.build();
	}

	@Bean
	public Step xmlItemWriterDemoStep() {
		return stepBuilderFactory.get("xmlItemWriterDemoStep")
				.<Customer, Customer>chunk(10)
				.reader(dbjdbcReader)
				.writer(xmlItemWriter)
				.build();
	}
	
	
	
}
