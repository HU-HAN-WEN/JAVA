package listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener {
	
	@Override
	public void beforeJob(JobExecution jobExecution) 
	{
		System.out.println(jobExecution.getJobInstance().getJobName()+"before...");
		
	}

	@Override
	public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName()+"after...");
		
	}

}
