package com.asynclife.batch;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.asynclife.batch.config.StandaloneInfrastructureConfiguration;
import com.asynclife.batch.jobs.FlatfileToDbJobConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={StandaloneInfrastructureConfiguration.class, FlatfileToDbJobConfiguration.class})
public class FlatFileBatchTest {
	
	Logger log = LoggerFactory.getLogger(FlatFileBatchTest.class);
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Resource(name="flatfileToDbJob")
	private Job job;
	
	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setUp() {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void test() throws Exception {
		log.info("flatFile2DB run...");
		jobLauncher.run(job, new JobParameters());
		Assert.isTrue(jdbcTemplate.queryForObject("select count(*) from Account",Integer.class) == 2);
	}
	
}
