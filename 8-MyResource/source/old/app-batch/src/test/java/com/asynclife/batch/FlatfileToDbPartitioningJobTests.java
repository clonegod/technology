package com.asynclife.batch;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.asynclife.batch.config.StandaloneInfrastructureConfiguration;
import com.asynclife.batch.jobs.FlatfileToDbPartitioningJobConfiguration;

@ContextConfiguration(classes={StandaloneInfrastructureConfiguration.class, 
		FlatfileToDbPartitioningJobConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class FlatfileToDbPartitioningJobTests {
	
	@Autowired
	private JobLauncher jobLauncher;

	@Resource(name="flatfileToDbPartitioningJob")
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
		jobLauncher.run(job, new JobParameters());
		Assert.isTrue(jdbcTemplate.queryForObject("select count(*) from Partner",Integer.class) == 18);
	}
	
}