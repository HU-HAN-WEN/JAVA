package com.npidata.itemreadermulti;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

// import 的作用就是為了在使用其他 package 的 class 時，提供一個清晰不含糊的路徑，避免命名衝突。
//import com.npidata.itemreaderxml.Customer; 

@Configuration
public class MultiFileItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
//	使用 @Value 搭配萬用字元 (*) 來注入所有符合檔名的資源
	@Value("classpath:/file*.txt")
	private Resource[] fileResources;
	
//	注入MultiFileWriter.java的ItemWriter程式碼
	@Autowired
	@Qualifier("multiFileWriter")
	private ItemWriter<? super Customer> multiFileWriter;
	
	@Bean
	public Job multiFileItemReaderDemoJob()
	{
		return jobBuilderFactory.get("multiFileItemReaderDemoJob")
				.start(multiFileItemReaderDemoStep())
				.build();
	}

	@Bean
	public Step multiFileItemReaderDemoStep() {
		return stepBuilderFactory.get("multiFileItemReaderDemoStep")
				.<Customer, Customer>chunk(10)
				.reader(multiFileItemReader())
				// 呼叫下面的 @Bean 方法來取得 writer
				.writer(multiFileWriter)
				.build();
	}

	@Bean
	@StepScope //指定範圍
	public MultiResourceItemReader<Customer> multiFileItemReader() {
		
		MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();
		
//		調用flatFileItemReader()方法
		reader.setDelegate(flatFileItemReader());
//		自哪些文件中讀取
		reader.setResources(fileResources);
		return reader;
	}
	
	//讀取多個文件需要借助讀取單個文件的功能
	@Bean
	@StepScope
	public FlatFileItemReader<Customer> flatFileItemReader() {
		
		//創建FlatFileItemReader的對象
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		//告訴她從哪個文件中讀取
		reader.setResource(new ClassPathResource("customer.txt"));
//		reader.setLinesToSkip(1);//跳過第一行
		
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
