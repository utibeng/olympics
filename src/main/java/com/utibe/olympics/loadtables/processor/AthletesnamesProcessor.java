package com.utibe.olympics.loadtables.processor;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import com.utibe.olympics.loadtables.output.tables.athletenames.AthleteNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;


public class AthletesnamesProcessor implements ItemProcessor<CsvAthleteRow, AthleteNames> {



    private static final Logger logger = LoggerFactory.getLogger(AthletesnamesProcessor.class);

    
    //private final JdbcTemplate jdbcTemplate;

    public AthletesnamesProcessor() {
    }

    @Override
    public AthleteNames process(final CsvAthleteRow csvAthleteRow) throws Exception {

        //logger.info("id to process is {}", csvAthleteRow.getId());

        //final int id = Integer.parseInt(csvAthleteRow.getId());
        final int id = csvAthleteRow.getId();
        final String name = csvAthleteRow.getName();

        final AthleteNames athleteNames = new AthleteNames(id, name);
        //logger.info("Converting (" + csvAthleteRow + ") into (" + athleteNames + ")");

        return athleteNames;
    }
}