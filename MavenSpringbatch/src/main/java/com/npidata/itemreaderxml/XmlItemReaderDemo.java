package com.npidata.itemreaderxml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.XStream;

@Configuration
public class XmlItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("xmlFileWriter")
	private ItemWriter<? super Customer> xmlFileWriter;
	
	@Bean
	public Job xmlItemReaderDemoJob()
	{
		return jobBuilderFactory.get("xmlItemReaderDemoJob")
				.start(xmlItemReaderDemoStep())
				.build();
	}

	@Bean
	public Step xmlItemReaderDemoStep() {
		
		return stepBuilderFactory.get("xmlItemReaderDemoStep")
				.<Customer, Customer>chunk(2)
				.reader(xmlFileReader())
				.writer(xmlFileWriter)
				.build();
	}

	@Bean
	@StepScope
	public StaxEventItemReader<Customer> xmlFileReader() {
		StaxEventItemReader<Customer> reader = new StaxEventItemReader<Customer>();
		reader.setResource(new ClassPathResource("customer.xml"));
		
		//指定需要處理的根標籤 --> <customer></customer>
		reader.setFragmentRootElementName("customer");
		
		//把讀取到的xml格式轉成customer對象
		//加上xstream、spring-oxm依賴，放到pom.xml中
		XStreamMarshaller unmarshaller = new XStreamMarshaller();
		
		//賦值給unmarshaller
		Map<String, Class<?>> map = new HashMap<>();
		map.put("customer", Customer.class);
		unmarshaller.setAliases(map);

		// 設定安全許可：明確告訴 XStream，允許轉換成 Customer 這個類別
//	    unmarshaller.setSupportedClasses(Customer.class);
		
		// =====================【修正開始】=====================
				// 為了處理 XStream 的 ForbiddenClassException 安全性限制，我們需要明確地允許反序列化的類別。
				// 這是解決 `com.thoughtworks.xstream.security.ForbiddenClassException` 的關鍵。
		try {
			XStream xstream = unmarshaller.getXStream();
			XStream.setupDefaultSecurity(xstream); // 啟用預設的安全設定
			xstream.allowTypes(new Class[]{Customer.class}); // 明確允許 Customer 類別
			// 或者，也可以允許整個套件 (package) 下的所有類別，更具彈性
			// xstream.allowTypesByWildcard(new String[] { "com.npidata.itemreaderxml.**" });
		} catch (Exception e) {
			// 在實際應用中，這裡應該有更完善的異常處理
			throw new RuntimeException("設定 XStream 安全性時發生錯誤", e);
		}
	    
		
		//unmarshaller給reader
		reader.setUnmarshaller(unmarshaller);
		
		//最後再返回reader對象
		return reader;
	}

}
