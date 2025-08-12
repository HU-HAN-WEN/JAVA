package com.npidata.itemreaderfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

@Configuration
public class FileItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job fileItemReaderDemoJob()
	{
		return jobBuilderFactory.get("fileItemReaderDemoJob")
				.start(fileItemReaderDemoStep())
				.build();
	}

	@Bean
	public Step fileItemReaderDemoStep() {
		
		return stepBuilderFactory.get("fileItemReaderDemoStep")
				.<Customer, Customer>chunk(10)
				.reader(flatFileItemReader()) // 呼叫方法
				.writer(flatFileItemWriter) // 直接使用變數
				.build();
	}

	//寫 .writer
	@Autowired
	@Qualifier("flatFileItemWriter")
	private ItemWriter<? super Customer> flatFileItemWriter;// 這是變數，不是方法

	//讀 .reader
	@Bean
	@StepScope
	public FlatFileItemReader<Customer> flatFileItemReader() {
		
		//創建FlatFileItemReader的對象
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		//告訴她從哪個文件中讀取
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);//跳過第一行
		
		//數據解析
		DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"id", "firstName", "lastName", "birthday"});
		
		//把解析出的一行數據映射為Customer對象
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		//怎麼映射
		mapper.setLineTokenizer(tokenizer);
		//進行映射
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstName(fieldSet.readString("firstName"));
				customer.setLastName(fieldSet.readString("lastName"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);				
		return reader;
	}
	
}
