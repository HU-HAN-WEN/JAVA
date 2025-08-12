package HanMavenTest.MavenSpringbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableBatchProcessing 
//一個專案中只需要一個，JobConfiguration.java 中已有，此處可省略

public class JobDemo {
	
	@Autowired
	//創建Job的對象
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	//創建Step的對象
	private StepBuilderFactory stepBuilderFactory;
	
	//創建任務，用 @Qualifier 來精準指定要注入哪個 Bean，避免混淆。
	@Bean
    public Job demoJob(@Qualifier("demoStep1") Step step1,
                       @Qualifier("demoStep2") Step step2,
                       @Qualifier("demoStep3") Step step3) {
        return jobBuilderFactory.get("demoJob") // Job 的名稱也建議修改，保持唯一性
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
//        		.start(step1())
        		//.on指定條件完成後到step2
//        		.on("COMPLETED").to(step2())
//        		.from(step2()).on("COMPLETED").to(step3())
//        		.from(step3()).end()
//        		.build();
	}

	/**
     * 使用 @Bean(name = "demoStep1") 來明確指定這個 Bean 的唯一名稱。
     * 這樣它就能被上面的 demoJob 方法透過 @Qualifier("demoStep1") 找到了。
     * 為了保持程式碼清晰，我們也將方法名稱從 step1 改為 demoStep1。
     */
    @Bean(name = "demoStep1")
    public Step demoStep1() {
        return stepBuilderFactory.get("step1") // Step 本身的名稱 (在 Batch 元資料中) 仍然可以是 "step1"
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Executing Demo Step 1 from JobDemo...");
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean(name = "demoStep2")
    public Step demoStep2() {
        return stepBuilderFactory.get("step2")
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Executing Demo Step 2 from JobDemo...");
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean(name = "demoStep3")
    public Step demoStep3() {
        return stepBuilderFactory.get("step3")
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Executing Demo Step 3 from JobDemo...");
                return RepeatStatus.FINISHED;
            }).build();
    }

}
