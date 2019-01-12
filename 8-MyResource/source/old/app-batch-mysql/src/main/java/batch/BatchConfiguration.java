package batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import batch.model.Person;
import batch.processor.PersonItemProcessor;
import batch.tokenizer.PersonFixedLengthTokenizer;


@SpringBootApplication
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class BatchConfiguration {

    /*
        Load the properties
     */
    @Value("${database.driver}")
    private String databaseDriver;
    @Value("${database.url}")
    private String databaseUrl;
    @Value("${database.username}")
    private String databaseUsername;
    @Value("${database.password}")
    private String databasePassword;

    /**
     * We define a bean that read each line of the input file.
     *
     * @return
     */
    @Bean
    public ItemReader<Person> reader() {
        // we read a flat file that will be used to fill a Person object
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        // we pass as parameter the flat file directory
        reader.setResource(new ClassPathResource("PersonData.txt"));
        // we use a default line mapper to assign the content of each line to the Person object
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            // we use a custom fixed line tokenizer
            setLineTokenizer(new PersonFixedLengthTokenizer());
            // as field mapper we use the name of the fields in the Person class
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                // we create an object Person
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    /**
     * The ItemProcessor is called after a new line is read and it allows the developer
     * to transform the data read
     * In our example it simply return the original object
     *
     * @return
     */
    @Bean
    public ItemProcessor<Person, Person> processor() {
        return new PersonItemProcessor();
    }

    /**
     * Nothing special here a simple JpaItemWriter
     * @return
     */
    @Bean
    public ItemWriter<Person> writer(DataSource dataSource) {
    	JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO person (first_name, family_name, year) VALUES (:firstName, :familyName, :year)");
        writer.setDataSource(dataSource);
        return writer;
    }
    

    /**
     * This method declare the steps that the batch has to follow
     *
     * @param jobs
     * @param s1
     * @return
     */
    @Bean
    public Job importPerson(JobBuilderFactory jobs, Step s1) {

        return jobs.get("importPerson")
                .incrementer(new RunIdIncrementer()) // because a spring config bug, this incrementer is not really useful
                .flow(s1)
                .end()
                .build();
    }


    /**
     * Step
     * We declare that every 1000 lines processed the data has to be committed
     *
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader,
                      ItemWriter<Person> writer, ItemProcessor<Person, Person> processor) {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * As data source we use an external database
     *
     * @return
     */

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }
    
//    @Bean
//    public PlatformTransactionManager transactionManager(){
//        return new DataSourceTransactionManager(dataSource());
//    }

}
