package com.utibe.olympics.loadtables.output.tables.olympicevents;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class OlympicEventsProcessor implements ItemProcessor<CsvAthleteRow, OlympicEvents> {

    private static final Logger logger = LoggerFactory.getLogger(OlympicEventsProcessor.class);

    public OlympicEventsProcessor() {
    }

    @Override
    public OlympicEvents process(CsvAthleteRow csvAthleteRow) {

        final String sport = csvAthleteRow.getSport();
        final String event = csvAthleteRow.getEvent();
        final String year = csvAthleteRow.getYear();
        final String games = csvAthleteRow.getGames();
        final String season = csvAthleteRow.getSeason();

        //logger.info("Converting (" + csvAthleteRow + ") into (" + athleteNames + ")");

        return new OlympicEvents(sport, event, year, games, season);
    }
}