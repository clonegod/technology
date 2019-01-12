package com.asynclife.batch.jobs;

import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.asynclife.batch.config.InfrastructureConfiguration;
import com.asynclife.batch.domain.Partner;
import com.asynclife.batch.listener.LogProcessListener;
import com.asynclife.batch.listener.ProtocolListener;
import com.asynclife.batch.processor.LogItemProcessor;

/**
 * @author Tobias Flohre
 */
@Configuration
@EnableBatchProcessing
public class FlatfileToDbPartitioningJobConfiguration {
	
	@Autowired
	private ResourcePatternResolver resourcePatternResolver;
	
	@Autowired
	private JobBuilderFactory jobBuilders;
	
	@Autowired
	private StepBuilderFactory stepBuilders;
	
	@Autowired
	private InfrastructureConfiguration infrastructureConfiguration;
	
	@Bean
	public Job flatfileToDbPartitioningJob(){
		return jobBuilders.get("flatfileToDbPartitioningJob")
				.listener(protocolListener())
				.start(partitionStep())
				.build();
	}
	
	@Bean
	public Step partitionStep(){
		return stepBuilders.get("partitionStep")
				.partitioner(flatfileToDbStep())
				.partitioner("flatfileToDbStep", partitioner())
				.taskExecutor(infrastructureConfiguration.taskExecutor())
				.build();
	}
	
	@Bean
	public Step flatfileToDbStep(){
		return stepBuilders.get("flatfileToDbStep")
				.<Partner,Partner>chunk(1)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.listener(logProcessListener())
				.build();
	}
	
	@Bean
	public Partitioner partitioner(){
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		Resource[] resources;
		try {
			resources = resourcePatternResolver.getResources("file:src/test/resources/*.csv");
		} catch (IOException e) {
			throw new RuntimeException("I/O problems when resolving the input file pattern.",e);
		}
		partitioner.setResources(resources);
		return partitioner;
	}
	
	// 必须明确指定1个Reader供所有partitioner线程使用
	// 对于读取文件而言，使用partitioner无实际意义
	// 应该将资源统一封装到BlockingQueue/消息队列中，由Reader提供统一访问接口，实现不同线程获取不同的任务进行处理
	@Bean
	public FlatFileItemReader<Partner> reader(){
		FlatFileItemReader<Partner> itemReader = new FlatFileItemReader<Partner>();
		itemReader.setLineMapper(lineMapper());
		itemReader.setResource(new ClassPathResource("partner-import.csv"));
		return itemReader;
	}
	
	@Bean
	public LineMapper<Partner> lineMapper(){
		DefaultLineMapper<Partner> lineMapper = new DefaultLineMapper<Partner>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[]{"name","email"});
		lineTokenizer.setIncludedFields(new int[]{0,2});
		BeanWrapperFieldSetMapper<Partner> fieldSetMapper = new BeanWrapperFieldSetMapper<Partner>();
		fieldSetMapper.setTargetType(Partner.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
	
	@Bean
	public ItemProcessor<Partner,Partner> processor(){
		return new LogItemProcessor<Partner>();
	}
	
	@Bean
	public ItemWriter<Partner> writer(){
		JdbcBatchItemWriter<Partner> itemWriter = new JdbcBatchItemWriter<Partner>();
		itemWriter.setSql("INSERT INTO PARTNER (NAME, EMAIL) VALUES (:name,:email)");
		itemWriter.setDataSource(infrastructureConfiguration.dataSource());
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Partner>());
		return itemWriter;
	}
	
	@Bean
	public ProtocolListener protocolListener(){
		return new ProtocolListener();
	}
	
	@Bean
	public LogProcessListener logProcessListener(){
		return new LogProcessListener();
	}
}
