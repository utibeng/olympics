package com.utibe.olympics.loadtables.config;

import javax.sql.DataSource;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import com.utibe.olympics.loadtables.output.tables.athletenames.AthleteNames;
import com.utibe.olympics.loadtables.processor.AthletesNamesFilterProcessor;
import com.utibe.olympics.loadtables.processor.AthletesnamesProcessor;
import com.utibe.olympics.loadtables.processor.AthletesNamesJobCompletionNotificationListener;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @ConditionalOnProperty(
            value="utibe.table.athletes.load",
            havingValue = "true",
            matchIfMissing = false
    )
    public Job importAthleteJob(AthletesNamesJobCompletionNotificationListener listener, Step step1) {
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
                .<CsvAthleteRow, AthleteNames> chunk(3)
                .reader(reader())
                .processor(compositeItemProcessor())
                //.processor(filterProcessor())
                //.processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<CsvAthleteRow> reader() {
        return new FlatFileItemReaderBuilder<CsvAthleteRow>()
                .name("personItemReader")
                .resource(new ClassPathResource("OLYMPICS_athlete_events.csv"))
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
    public ItemProcessor<CsvAthleteRow, AthleteNames> compositeItemProcessor() {
        CompositeItemProcessor<CsvAthleteRow, AthleteNames> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(filterProcessor(), processor()));
        return compositeItemProcessor;
    }


    @Bean
    public AthletesnamesProcessor processor() {
        return new AthletesnamesProcessor();
    }

    @Bean
    public AthletesNamesFilterProcessor filterProcessor() {
        return new AthletesNamesFilterProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<AthleteNames> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AthleteNames>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO olympics_athletesnames (id, name) VALUES (:id, :name)")
                //.sql("select id, name from athletesnames ")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]


    // end::jobstep[]
}
