package com.asynclife.batch;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.asynclife.batch.config.StandaloneInfrastructureConfiguration;
import com.asynclife.batch.jobs.FlatfileToDbWithParametersJobConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={StandaloneInfrastructureConfiguration.class, FlatfileToDbWithParametersJobConfiguration.class})
public class FlatfileToDbWithParametersJobTests {
	
	@Autowired
	private JobLauncher jobLauncher;

	@Resource(name="flatfileToDbWithParametersJob")
	private Job job;
	
	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup(){
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void testLaunchJob() throws Exception {
		// #{jobParameters[xy]}
		// #{jobExecutionContext[xy]}
		// #{stepExecutionContext[xy]}
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("pathToFile", "account/20150920_02.csv")
			.toJobParameters();
		jobLauncher.run(job, jobParameters);
		Assert.isTrue(jdbcTemplate.queryForObject("select count(*) from Account",Integer.class) == 3);
	}
	
}