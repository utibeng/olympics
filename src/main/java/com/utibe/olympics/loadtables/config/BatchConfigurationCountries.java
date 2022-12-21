package com.utibe.olympics.loadtables.config;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import com.utibe.olympics.loadtables.output.tables.countries.Countries;
import com.utibe.olympics.loadtables.output.tables.countries.CountriesFilterProcessor;
import com.utibe.olympics.loadtables.output.tables.countries.CountriesJobCompletionNotificationListener;
import com.utibe.olympics.loadtables.output.tables.countries.CountriesProcessor;
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
public class BatchConfigurationCountries {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @ConditionalOnProperty(
            value="utibe.table.countries.load",
            havingValue = "true"
    )
    public Job importCountriesJob(CountriesJobCompletionNotificationListener listener,
                                  Step step1Countries) {
        return jobBuilderFactory.get("importCountries")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1Countries)
                .end()
                .build();
    }

    @Bean
    public Step step1Countries(JdbcBatchItemWriter<Countries> writerCountries) {
        return stepBuilderFactory.get("step1Countries")
                .<CsvAthleteRow, Countries> chunk(3)
                .reader(readerCountries())
                .processor(compositeItemProcessorCountries())
                .writer(writerCountries)
                .build();
    }

    @Bean
    public FlatFileItemReader<CsvAthleteRow> readerCountries() {
        return new FlatFileItemReaderBuilder<CsvAthleteRow>()
                .name("CountriesReader")
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
    public ItemProcessor<CsvAthleteRow, Countries> compositeItemProcessorCountries() {
        CompositeItemProcessor<CsvAthleteRow, Countries> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(filterProcessorCountries(), processorCountries()));
        return compositeItemProcessor;
    }

    @Bean
    public CountriesProcessor processorCountries() {
        return new CountriesProcessor();
    }

    @Bean
    public CountriesFilterProcessor filterProcessorCountries() {
        return new CountriesFilterProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Countries> writerCountries(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Countries>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO olympics_countries (noc, team ) " + "VALUES (:noc, :team)")
                .dataSource(dataSource)
                .build();
    }

}
