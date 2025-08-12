package com.npidata.multifile;

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
public class MultiFileItemWriterDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<Customer> dbjdbcReader; // 泛型可以更精確
	
	@Autowired
	@Qualifier("multiFileItemWriter")
	private ItemWriter<Customer> multiFileItemWriter; // 泛型可以更精確
	
	@Bean
	public Job multiFileItemWriterDemoJob1() 
	{
		return jobBuilderFactory.get("multiFileItemWriterDemoJob1")
				.start(multiFileItemWriterDemoStep())
				.build();
	}

	@Bean
	public Step multiFileItemWriterDemoStep() {
		return stepBuilderFactory.get("multiFileItemWriterDemoStep")
				.<Customer, Customer>chunk(10)
				.reader(dbjdbcReader)
				.writer(multiFileItemWriter)
				.build();
	}
	
	
}
