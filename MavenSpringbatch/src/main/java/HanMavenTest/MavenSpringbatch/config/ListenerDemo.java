package HanMavenTest.MavenSpringbatch.config;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import listener.MyChunkListener;
import listener.MyJobListener;

@Configuration
public class ListenerDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	/**
	 * 定義一個 Job Bean。
	 * 這個 Job 依賴於一個 Step 類型的 Bean。
	 * 我們透過方法參數 `Step step1` 來告訴 Spring：「請幫我找到名為 step1 的 Step Bean 並注入進來」。
	 */
	@Bean
	public Job listenerJob(Step listenerStep1)
	{
		return jobBuilderFactory.get("listenerJob")
				.listener(new MyJobListener())
				.start(listenerStep1)// 【修正】直接使用傳入的 listenerStep1 參數，而不是呼叫 listenerStep1() 方法
				.build();
	}
	
	/**
	 * 定義一個 Step Bean，並命名為 "listenerStep1"。
	 * 這個 Step 依賴於 ItemReader 和 ItemWriter。
	 * 同樣地，我們透過方法參數讓 Spring 自動注入 reader 和 writer。
	 */
	@Bean
	public Step listenerStep1(ItemReader<String> reader, ItemWriter<String> writer) {
		return stepBuilderFactory.get("listenerStep1")
				.<String, String>chunk(2) // 一次讀取和處理2個項目
				.faultTolerant() // 啟用容錯功能
				.listener(new MyChunkListener())
				.reader(reader) // 使用注入的 reader Bean
				.writer(writer) // 使用注入的 writer Bean
				.build();
    }

	/**
	 * 定義一個 ItemWriter Bean，負責「寫」資料。
	 * 將方法設為 public。
	 */
	@Bean
	public ItemWriter<String> write() {
		// 這裡使用 Lambda 表示式讓程式碼更簡潔
				return items -> {
					System.out.println("本次寫入的資料是: " + items);
				};
	}

	/**
	 * 定義一個 ItemReader Bean，負責「讀」資料。
	 * 將方法設為 public。
	 */
	@Bean
	public ItemReader<String> read() 
	{
		// 為了方便測試，我們從一個固定的 List 讀取資料
		return new ListItemReader<>(Arrays.asList("java", "spring", "mybatis", "batch", "maven", "hanwen"));
	}

}
