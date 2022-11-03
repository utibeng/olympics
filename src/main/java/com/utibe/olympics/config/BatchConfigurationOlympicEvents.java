package com.utibe.olympics.config;

import com.utibe.olympics.input.CsvAthleteRow;
import com.utibe.olympics.output.tables.olympicevents.OlympicEvents;
import com.utibe.olympics.output.tables.olympicevents.OlympicEventsFilterProcessor;
import com.utibe.olympics.output.tables.olympicevents.OlympicEventsJobCompletionNotificationListener;
import com.utibe.olympics.output.tables.olympicevents.OlympicEventsProcessor;
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
public class BatchConfigurationOlympicEvents {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @ConditionalOnProperty(
            value="utibe.table.olympicevents.load",
            havingValue = "true"
    )
    public Job importEventGamesJobOlympicEvents(OlympicEventsJobCompletionNotificationListener listener,
                                                Step step1OlympicEvents) {
        return jobBuilderFactory.get("importEventGames")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1OlympicEvents)
                .end()
                .build();
    }

    @Bean
    public Step step1OlympicEvents(JdbcBatchItemWriter<OlympicEvents> writerOlympicEvents) {
        return stepBuilderFactory.get("step1OlympicEvents")
                .<CsvAthleteRow, OlympicEvents> chunk(3)
                .reader(readerOlympicEvents())
                .processor(compositeItemProcessorOlympicEvents())
                //.processor(processorOlympicEvents())
                .writer(writerOlympicEvents)
                .build();
    }

    @Bean
    public FlatFileItemReader<CsvAthleteRow> readerOlympicEvents() {
        return new FlatFileItemReaderBuilder<CsvAthleteRow>()
                .name("olympicEventReader")
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
    public ItemProcessor<CsvAthleteRow, OlympicEvents> compositeItemProcessorOlympicEvents() {
        CompositeItemProcessor<CsvAthleteRow, OlympicEvents> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(filterProcessorOlympicEvents(), processorOlympicEvents()));
        return compositeItemProcessor;
    }


    @Bean
    public OlympicEventsProcessor processorOlympicEvents() {
        return new OlympicEventsProcessor();
    }

    @Bean
    public OlympicEventsFilterProcessor filterProcessorOlympicEvents() {
        return new OlympicEventsFilterProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<OlympicEvents> writerOlympicEvents(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<OlympicEvents>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO olympicevents (sport, event, year, games, season) " +
                        "VALUES (:sport, :event, :year, :games, :season)")
                .dataSource(dataSource)
                .build();
    }

}
