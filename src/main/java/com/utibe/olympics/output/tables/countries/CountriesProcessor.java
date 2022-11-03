package com.utibe.olympics.output.tables.countries;

import com.utibe.olympics.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class CountriesProcessor implements ItemProcessor<CsvAthleteRow, Countries> {

    private static final Logger logger = LoggerFactory.getLogger(CountriesProcessor.class);

    public CountriesProcessor() {
    }

    @Override
    public Countries process(CsvAthleteRow csvAthleteRow) {

        final String noc = csvAthleteRow.getNoc();
        final String team = csvAthleteRow.getTeam();


        return new Countries(noc, team);

    }
}