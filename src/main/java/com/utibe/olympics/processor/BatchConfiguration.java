package com.utibe.olympics.processor;

import javax.sql.DataSource;

import com.utibe.olympics.input.CsvAthleteRow;
import com.utibe.olympics.output.tables.athletenames.AthleteNames;
import com.utibe.olympics.output.tables.athletenames.AthletesnamesProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    // end::setup[]

    // tag::readerwriterprocessor[]

    //String id, String name, String sex, String age, String height, String weight,
    //                         String team, String noc, String games, String year, String season, String city,
    //                         String sport, String event, String medal
    @Bean
    public FlatFileItemReader<CsvAthleteRow> reader() {
        return new FlatFileItemReaderBuilder<CsvAthleteRow>()
                .name("personItemReader")
                .resource(new ClassPathResource("test.csv"))
                .delimited()
                .names("id", "name", "sex", "age", "height", "weight", "team", "noc", "games",
                        "year", "season", "city", "sport", "event", "medal")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(CsvAthleteRow.class);
                }})
                .build();
    }

    @Bean
    public AthletesnamesProcessor processor() {
        return new AthletesnamesProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<AthleteNames> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AthleteNames>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO athletesnames (id, name) VALUES (:id, :name)")
                //.sql("select id, name from athletesnames ")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(AthletesNamesJobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<AthleteNames> writer) {
        return stepBuilderFactory.get("step1")
                .<CsvAthleteRow, AthleteNames> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}
