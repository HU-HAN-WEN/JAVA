package com.npidata.itemreader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class ItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job itemReaderDemoJob() 
	{
		return jobBuilderFactory.get("itemReaderDemoJob")
				.start(itemReaderDemoStep())
				.build();
	}

	@Bean
	public Step itemReaderDemoStep() {
		return stepBuilderFactory.get("itemReaderDemoStep")
				.<String, String>chunk(2)
				.reader(itemReaderDemoRead())
				.writer(list->{
					for(String item:list) 
					{
						System.out.println(item+"...");
					}
				}).build();
				
	}

//	原始
//	@Bean
//	public ItemReader<String> itemReaderDemoRead() {
//		
//		return null;
//	}
//	自定義類型
	@Bean
	public MyReader itemReaderDemoRead() {
		List<String> data=Arrays.asList("cat", "dog", "pig", "duck");
		return new MyReader(data);
	}
	
}
