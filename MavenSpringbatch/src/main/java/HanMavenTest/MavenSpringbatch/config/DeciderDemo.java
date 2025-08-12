package HanMavenTest.MavenSpringbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing

public class DeciderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
//	創建step
	@Bean
	public Step deciderDemoStep1() 
	{
		return stepBuilderFactory.get("deciderDemoStep1")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("deciderDemoStep1");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
	@Bean
	public Step deciderDemoStep2() 
	{
		return stepBuilderFactory.get("deciderDemoStep2")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("even");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
	@Bean
	public Step deciderDemoStep3() 
	{
		return stepBuilderFactory.get("deciderDemoStep3")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("odd");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
//	創建決策器
	@Bean
	public JobExecutionDecider myDecider() 
	{
		return new MyDecider();
	}
	
//	創建任務
	@Bean
	public Job deciderDemoJob() 
	{
		return jobBuilderFactory.get("deciderDemoJob")
				.start(deciderDemoStep1())
//				接下來是step2還是step3，是根據決策器
				.next(myDecider())
				.from(myDecider()).on("even").to(deciderDemoStep2())
				.from(myDecider()).on("odd").to(deciderDemoStep3())
				.from(deciderDemoStep3()).on("*").to(myDecider()) //無論是甚麼回答都會返還到決策器
				.end()
				.build();
				
	}
	
	

}
