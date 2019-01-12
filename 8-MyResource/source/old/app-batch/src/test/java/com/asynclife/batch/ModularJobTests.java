package com.asynclife.batch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.asynclife.batch.config.ModularJobConfiguration;
import com.asynclife.batch.config.StandaloneInfrastructureConfiguration;

/**
 * @author Tobias Flohre
 */
@ContextConfiguration(classes={ModularJobConfiguration.class,
		StandaloneInfrastructureConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModularJobTests {
	
	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup(){
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void testLaunchJob() throws Exception {
		
		// Job1
		Job flatfileToDbJob = jobRegistry.getJob("flatfileToDbJob");
		jobLauncher.run(flatfileToDbJob, new JobParameters());
		Assert.isTrue(jdbcTemplate.queryForObject("select count(*) from Account",Integer.class) == 2);
		
		// Job2
		Job parametersJob = jobRegistry.getJob("flatfileToDbWithParametersJob");
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("pathToFile", "account/20150920_02.csv")
			.toJobParameters();
		jobLauncher.run(parametersJob, jobParameters);
		assertThat(parametersJob.getName(),is("flatfileToDbWithParametersJob"));
		Assert.isTrue(jdbcTemplate.queryForObject("select count(*) from Account",Integer.class) > 2);
	}
	
}
