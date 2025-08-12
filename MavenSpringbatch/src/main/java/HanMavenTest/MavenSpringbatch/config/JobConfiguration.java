package HanMavenTest.MavenSpringbatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer; // 1. 匯入我們需要的工具
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {
	private static final Logger log = LoggerFactory.getLogger(JobConfiguration.class);
	
	// 使用 @Autowired 讓 Spring 自動注入工廠物件
	@Autowired
	//注入創建任務對象的對象
	private JobBuilderFactory jobBuilderFactory;
	
	//任務的執行由Step決定
	//注入創建Step對象的對象
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	//創建任務對象
	@Bean
	public Job helloWorldJob(Step helloWorldStep1){ // 參數名稱對應到下面的 Bean 名稱
		return jobBuilderFactory.get("helloWorldJob")
				.incrementer(new RunIdIncrementer())
				.start(helloWorldStep1) // 使用注入的 Step Bean
				.build();
	}

	@Bean
	public Step helloWorldStep1() {
		return stepBuilderFactory.get("step1")
		       .tasklet(new Tasklet() {
					
					@Override
					 public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // 3. 使用日誌框架輸出，這在正式開發中是更好的習慣
                        log.info("====================================================");
                        log.info("Hello, World! 這是一個 Spring Batch 任務！");
                        log.info("任務參數 (Job Parameters): " + chunkContext.getStepContext().getJobParameters());
                        log.info("====================================================");
                        return RepeatStatus.FINISHED;
					}
				}).build();
	}

	



}