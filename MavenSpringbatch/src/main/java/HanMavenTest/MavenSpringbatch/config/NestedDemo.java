package HanMavenTest.MavenSpringbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class NestedDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
	
//	注入子Job
	@Autowired
	private Job childJobOne;
	@Autowired
	private Job childJobTwo;
	
//	注入啟動象
	@Autowired
	private JobLauncher launcher;
	
	@Bean
	public Job parentJob(JobRepository repository, PlatformTransactionManager transactionManager)
	{
		return jobBuilderFactory.get("parentJob")
				.start(childJob1(repository, transactionManager))
				.next(childJob2(repository, transactionManager))
				.build();
	}

//	返回的是Job類型的Step，這是特殊的Step
	private Step childJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob1"))
		.job(childJobOne)
		.launcher(launcher) //使用啟動父Job的啟動對象
		.repository(repository) //指定持久化存儲的對象
		.transactionManager(transactionManager)
		.build();
	}
	
	private Step childJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob2"))
		.job(childJobTwo)
		.launcher(launcher) //使用啟動父Job的啟動對象
		.repository(repository) //指定持久化存儲的對象
		.transactionManager(transactionManager)
		.build();
	}

}
