1. spring-batch UnitTest
	注入JobLauncherTestUtils失败的解决办法：
	在任意Configuration中，手动发布1个Bean
	@Bean
	public JobLauncherTestUtils forTest() {
		return new JobLauncherTestUtils();
	}	
	
	测试整个Job
	测试某个Step
	
2.JobParameters注入时为空的问题
	解决办法：
	被注入方法需要标注@StepScope，
	在step真正执行时通过reader(@Value("#{jobParameters[filepath]}") String file2Read)取到参数
	注意：参数名不能有'.'，否则被视为对象导航，从而报错
	
3.Spring Batch metadata tables 
	会自动根据ApplicationContext中注册的DataSource类型，判断数据库
	如果DataSource类型为MYSQL，则会读取batch-core包下的schema-mysql.sql初始化JOB相关表到MYSQL中
	
cursor based reading 
	
4. how to use transaction, how to handle roll back? 
 
5. how to restarting a batch?

6. how to use skip and retry functionality?
	
7. how to use listener for monitor job/step execution status?	

8. 关闭JOB在application启动后自动执行
#prevent auto running jobs when application start.
#Or you can use spring.batch.job.names, it takes a comma-delimited list of job names that will be run.
spring.batch.job.enabled=false
	
	
https://blog.codecentric.de/en/2012/03/transactions-in-spring-batch-part-1-the-basics/
https://blog.codecentric.de/en/2012/03/transactions-in-spring-batch-part-2-restart-cursor-based-reading-and-listeners/
https://blog.codecentric.de/en/2012/03/transactions-in-spring-batch-part-3-skip-and-retry/
	
	
