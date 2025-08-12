package com.npidata.multifile;

//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
//import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MultiFileItemWriterConfig {
	
		
//		@Bean
//		public FlatFileItemWriter<Customer> fileItemWriter() throws Exception
//		{
////			把Customer對象轉成字符串輸出到文件
//			FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<Customer>();
//			
////			寫入到哪個文件(位置)
//			String path="D:\\storage\\datasets\\springbatch\\customer1.txt";
//			writer.setResource(new FileSystemResource(path));
//			
////			把Customer對象轉成字符
//			writer.setLineAggregator(new LineAggregator<Customer>() {
//				
//				ObjectMapper mapper = new ObjectMapper();
//				@Override
//				public String aggregate(Customer item) {
//					String str = null;
//					try {
//						str = mapper.writeValueAsString(item);
//					} catch (JsonProcessingException e) {
//						e.printStackTrace();
//					}
//					return str;
//				}
//			});
//			
//			writer.afterPropertiesSet();
//			return writer;
//		}
		
	   // Bean: 將資料寫入普通文字檔 (JSON格式)
    @Bean
    public FlatFileItemWriter<Customer> fileItemWriter() throws Exception {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        String path = "D:\\storage\\datasets\\springbatch\\customer_even.txt"; //偶數ID的客戶
        writer.setResource(new FileSystemResource(path));
        writer.setEncoding("UTF-8");
        writer.setLineAggregator(new LineAggregator<Customer>() {
            private final ObjectMapper mapper = new ObjectMapper();
            @Override
            public String aggregate(Customer item) {
                try {
                    return mapper.writeValueAsString(item);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("無法將物件轉換為JSON字串", e);
                }
            }
        });
        // Spring 會自動管理 afterPropertiesSet，通常不需手動呼叫
        return writer;
    }
	
	
	
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
		String path = "D:\\storage\\datasets\\springbatch\\cus1.xml";
		writer.setResource(new FileSystemResource(path));
		writer.afterPropertiesSet();
		
		return writer;
	}
		
//	//輸出數據到多個文件(.txt .xml)
//	@Bean
//	public CompositeItemWriter<Customer> multiFileItemWriter(
//			// 使用依賴注入，而不是直接呼叫方法
//			 FlatFileItemWriter<Customer> fileItemWriter,
//	         StaxEventItemWriter<Customer> xmlItemWriter
//			){
//		//創建對象
//		CompositeItemWriter<Customer> writer = new  CompositeItemWriter<Customer>();
//		
//		//將 Spring 注入的 Writer Bean 設置給 CompositeItemWriter
//		writer.setDelegates(Arrays.asList(fileItemWriter, xmlItemWriter));
//		
//		return writer;
//	}
	
	
	// Bean: 分類寫入器 (ClassifierCompositeItemWriter)
    @Bean
    public ClassifierCompositeItemWriter<Customer> multiFileItemWriter(
            // 關鍵修正1：使用依賴注入，將兩個 Writer Bean 作為參數傳入
            @Qualifier("fileItemWriter") ItemWriter<Customer> fileItemWriter,
            @Qualifier("xmlItemWriter") ItemWriter<Customer> xmlItemWriter) {
                
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<>();
		
     // 設定分類邏輯
        writer.setClassifier(customer -> customer.getId() % 2 == 0 ? fileItemWriter : xmlItemWriter);
        
		return writer;
	}
	
}
