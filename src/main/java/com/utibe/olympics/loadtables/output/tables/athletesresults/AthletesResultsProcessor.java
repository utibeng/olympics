package com.utibe.olympics.loadtables.output.tables.athletesresults;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class AthletesResultsProcessor implements ItemProcessor<CsvAthleteRow, AthletesResults> {

    private static final Logger logger = LoggerFactory.getLogger(AthletesResultsProcessor.class);

    public AthletesResultsProcessor() {
    }

    @Override
    public AthletesResults process(CsvAthleteRow csvAthleteRow) {
        final int id = csvAthleteRow.getId();
        final String noc = csvAthleteRow.getNoc();
        final String games = csvAthleteRow.getGames();
        final String event = csvAthleteRow.getEvent();
        final String medal = csvAthleteRow.getMedal();
        final String sex = csvAthleteRow.getSex();
        final String age = csvAthleteRow.getAge();
        final String height = csvAthleteRow.getHeight();
        final String weight = csvAthleteRow.getWeight();

        //logger.info("Converting (" + csvAthleteRow + ") into (" + athleteNames + ")");

        return new AthletesResults(id, noc, games, event, medal, sex, age, height, weight);

    }
}