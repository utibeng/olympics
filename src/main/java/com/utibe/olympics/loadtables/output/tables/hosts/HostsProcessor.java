package com.utibe.olympics.loadtables.output.tables.hosts;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class HostsProcessor implements ItemProcessor<CsvAthleteRow, Hosts> {

    private static final Logger logger = LoggerFactory.getLogger(HostsProcessor.class);

    public HostsProcessor() {
    }

    @Override
    public Hosts process(CsvAthleteRow csvAthleteRow) {

        final String games = csvAthleteRow.getGames();
        final String year= csvAthleteRow.getYear();
        final String season = csvAthleteRow.getSeason();
        final String city = csvAthleteRow.getCity();

        return new Hosts(games, year, season, city);
    }
}