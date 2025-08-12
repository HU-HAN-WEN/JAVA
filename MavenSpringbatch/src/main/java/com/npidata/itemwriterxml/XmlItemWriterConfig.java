package com.npidata.itemwriterxml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class XmlItemWriterConfig {

	@Bean
	public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception
	{
		StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<Customer>();
		
//		把對象轉為XML
		XStreamMarshaller marshaller = new XStreamMarshaller();
//		指定轉為XML的每個節點標籤
		Map<String, Class<?>> aliases = new HashMap<>();
//		指定XML每個節點標籤的名字
		aliases.put("customer", Customer.class);
		marshaller.setAliases(aliases);
		
//		指定XML的根標籤
		writer.setRootTagName("customers");
		writer.setMarshaller(marshaller);
		
//		指定寫到哪個XML文件
		String path = "D:\\storage\\datasets\\springbatch\\cus.xml";
		writer.setResource(new FileSystemResource(path));
		writer.afterPropertiesSet();
		
		return writer;
	}
}
