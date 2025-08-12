package com.npidata.itemwriterfile;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class FileItemWriterConfig {
	
	@Bean
	public FlatFileItemWriter<Customer> fileItemWriter() throws Exception
	{
//		把Customer對象轉成字符串輸出到文件
		FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<Customer>();
		
//		寫入到哪個文件(位置)
		String path="D:\\storage\\datasets\\springbatch\\customer.txt";
		writer.setResource(new FileSystemResource(path));
		
//		把Customer對象轉成字符
		writer.setLineAggregator(new LineAggregator<Customer>() {
			
			ObjectMapper mapper = new ObjectMapper();
			@Override
			public String aggregate(Customer item) {
				String str = null;
				try {
					str = mapper.writeValueAsString(item);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return str;
			}
		});
		
		writer.afterPropertiesSet();
		return writer;
	}
	
}
