package com.npidata.itemprocessor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemProcessorDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<Customer> dbJdbcReader; // 泛型可以更精確
	
	@Autowired
	@Qualifier("customerFileWriterConfig")
	private ItemWriter<Customer> customerFileWriterConfig; // 泛型可以更精確
	
//	一種處理方式
	@Autowired
	@Qualifier("firstNameUpperProcessor")
	private ItemProcessor<Customer, Customer> firstNameUpperProcessor;
	
//	指定另一種處理方式
	@Autowired
	@Qualifier("idFilterProcessor")
	private ItemProcessor<Customer, Customer> idFilterProcessor;
	
	
	@Bean
	public Job itemProcessorDemoJob() {
		return jobBuilderFactory.get("itemProcessorDemoJob")
				.start(itemProcessorDemoStep())
				.build();
	}

	@Bean
	public Step itemProcessorDemoStep() {
		return stepBuilderFactory.get("itemProcessorDemoStep")
				.<Customer, Customer>chunk(10)
				.reader(dbJdbcReader)
				.processor(process())
				.writer(customerFileWriterConfig)
				.build();
	}

	//如果有多種處理數據方式，該如何解決
	
	@Bean
	public CompositeItemProcessor<Customer, Customer> process()
	{
		CompositeItemProcessor<Customer, Customer> processor = new CompositeItemProcessor<Customer, Customer>();
		
//		多種處理方式來創建集合
		List<ItemProcessor<Customer, Customer>> delegates=new ArrayList<>();
		delegates.add(firstNameUpperProcessor);
		delegates.add(idFilterProcessor);
		
//		把所有集合方式放到處理當中
		processor.setDelegates(delegates);
		
		return processor;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
