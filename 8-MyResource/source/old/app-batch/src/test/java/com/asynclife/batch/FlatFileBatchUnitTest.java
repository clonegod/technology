package com.asynclife.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.asynclife.batch.config.StandaloneInfrastructureConfiguration;
import com.asynclife.batch.jobs.FlatfileToDbJobConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={StandaloneInfrastructureConfiguration.class, FlatfileToDbJobConfiguration.class})
public class FlatFileBatchUnitTest {
	
	Logger log = LoggerFactory.getLogger(FlatFileBatchUnitTest.class);
	
	// 需手动发布一个Bean，再注入
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Test
	public void test() throws Exception {
		log.info("flatFile2DB run...");
		//testing a job
		JobParameters jobParameters = new JobParametersBuilder().addString("filepath", "abc")
				.toJobParameters();
		JobExecution jobExecution1 = jobLauncherTestUtils.launchJob(jobParameters);
		Assert.isTrue(BatchStatus.COMPLETED==jobExecution1.getStatus());
		
		//Testing a individual step
		//JobExecution jobExecution2 = jobLauncherTestUtils.launchStep("step");
		//Assert.isTrue(BatchStatus.COMPLETED==jobExecution2.getStatus());
	}
	
}
