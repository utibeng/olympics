package com.utibe.olympics.loadtables.config;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import com.utibe.olympics.loadtables.output.tables.athletesresults.AthletesResults;
import com.utibe.olympics.loadtables.output.tables.athletesresults.AthletesResultsFilterProcessor;
import com.utibe.olympics.loadtables.output.tables.athletesresults.AthletesResultsJobCompletionNotificationListener;
import com.utibe.olympics.loadtables.output.tables.athletesresults.AthletesResultsProcessor;
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

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
public class BatchConfigurationAthletesResults {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @ConditionalOnProperty(
            value="utibe.table.athletesresults.load",
            havingValue = "true"
    )
    public Job importAthletesResultsJobAthletesResults(AthletesResultsJobCompletionNotificationListener listener,
                                                     Step step1AthletesResults) {
        return jobBuilderFactory.get("importAthletesResults")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1AthletesResults)
                .end()
                .build();
    }
    //

    @Bean
    public Step step1AthletesResults(JdbcBatchItemWriter<AthletesResults> writerAthletesResults) {
        return stepBuilderFactory.get("step1AthletesResults")
                .<CsvAthleteRow, AthletesResults> chunk(3)
                .reader(readerAthletesResults())
                .processor(compositeItemProcessorAthletesResults())
                .writer(writerAthletesResults)
                .build();
    } //

    @Bean
    public FlatFileItemReader<CsvAthleteRow> readerAthletesResults() {
        return new FlatFileItemReaderBuilder<CsvAthleteRow>()
                .name("AthletesResultsReader")
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
    public ItemProcessor<CsvAthleteRow, AthletesResults> compositeItemProcessorAthletesResults() {
        CompositeItemProcessor<CsvAthleteRow, AthletesResults> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(filterProcessorAthletesResults(), processorAthletesResults()));
        return compositeItemProcessor;
    }

    @Bean
    public AthletesResultsProcessor processorAthletesResults() {
        return new AthletesResultsProcessor();
    }

    @Bean
    public AthletesResultsFilterProcessor filterProcessorAthletesResults() {
        return new AthletesResultsFilterProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<AthletesResults> writerAthletesResults(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AthletesResults>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO olympics_athletesresults (id, noc, games, event, medal, sex, age, height, weight) " +
                        "VALUES (:id, :noc, :games, :event, :medal, :sex, :age, :height, :weight)")
                .dataSource(dataSource)
                .build();
    }

}
