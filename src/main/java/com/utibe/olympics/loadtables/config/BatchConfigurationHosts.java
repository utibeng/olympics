package com.utibe.olympics.loadtables.config;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import com.utibe.olympics.loadtables.output.tables.hosts.Hosts;
import com.utibe.olympics.loadtables.output.tables.hosts.HostsFilterProcessor;
import com.utibe.olympics.loadtables.output.tables.hosts.HostsJobCompletionNotificationListener;
import com.utibe.olympics.loadtables.output.tables.hosts.HostsProcessor;
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
public class BatchConfigurationHosts {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @ConditionalOnProperty(
            value="utibe.table.hosts.load",
            havingValue = "true"
    )
    public Job importHostsJob(HostsJobCompletionNotificationListener listener,
                                             Step step1Hosts) {
        return jobBuilderFactory.get("importHosts")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1Hosts)
                .end()
                .build();
    }

    @Bean
    public Step step1Hosts(JdbcBatchItemWriter<Hosts> writerHosts) {
        return stepBuilderFactory.get("step1Hosts")
                .<CsvAthleteRow, Hosts> chunk(3)
                .reader(readerHosts())
                .processor(compositeItemProcessorHosts())
                .writer(writerHosts)
                .build();
    }

    @Bean
    public FlatFileItemReader<CsvAthleteRow> readerHosts() {
        return new FlatFileItemReaderBuilder<CsvAthleteRow>()
                .name("HostsReader")
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
    public ItemProcessor<CsvAthleteRow, Hosts> compositeItemProcessorHosts() {
        CompositeItemProcessor<CsvAthleteRow, Hosts> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(filterProcessorHosts(), processorHosts()));
        return compositeItemProcessor;
    }

    @Bean
    public HostsProcessor processorHosts() {
        return new HostsProcessor();
    }

    @Bean
    public HostsFilterProcessor filterProcessorHosts() {
        return new HostsFilterProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Hosts> writerHosts(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Hosts>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO hosts (games, year, season, city ) " +
                        "VALUES (:games, :year, :season, :city)")
                .dataSource(dataSource)
                .build();
    }

}
