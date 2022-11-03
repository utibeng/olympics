package com.utibe.olympics.output.tables.athletesresults;

import com.utibe.olympics.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class AthletesResultsFilterProcessor implements ItemProcessor<CsvAthleteRow, CsvAthleteRow> {

    private static final Logger logger = LoggerFactory.getLogger(AthletesResultsFilterProcessor.class);

    private final Set<String> processedAthletesResults = new HashSet<>();

    public AthletesResultsFilterProcessor() {
    }

    @Override
    public CsvAthleteRow process(final CsvAthleteRow csvAthleteRow) throws Exception {
        //logger.info("hashset of Ids is {}", Arrays.toString(processedIds.toArray()) );

        String athletesResultsKey = csvAthleteRow.getId() + csvAthleteRow.getGames() + csvAthleteRow.getEvent();

        if(processedAthletesResults.contains(athletesResultsKey) ){
            logger.info("trapped duplicate for athletesResultsKey {}", athletesResultsKey);
            return null;
        }
        processedAthletesResults.add(athletesResultsKey);
        //logger.info(" Id {} not duplicated", csvAthleteRow.getId());

        return csvAthleteRow;
    }

}
