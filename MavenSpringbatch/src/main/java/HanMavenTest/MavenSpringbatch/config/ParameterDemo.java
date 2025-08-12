package HanMavenTest.MavenSpringbatch.config;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterDemo implements StepExecutionListener{
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	private Map<String, JobParameter> parameters;
	
	@Bean
	public Job parameterJob() 
	{
		return jobBuilderFactory.get("parameterJob")
				.start(parameterStep())
				.build();
	}
	
//	Job執行的是step，Job使用的數據肯定是在step中使用
//	那我們只需要給step傳遞數據
//	問題：如何給step傳遞參數？
//	可以使用step級別的監聽方式來傳遞數據
	@Bean
	public Step parameterStep() {
		
		return stepBuilderFactory.get("parameterStep")
				.listener(this)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						//輸出接收到的參數值
						System.out.println(parameters.get("info"));
						return RepeatStatus.FINISHED;
					}
				}).build();
				
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		parameters = stepExecution.getJobParameters().getParameters();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}
}
