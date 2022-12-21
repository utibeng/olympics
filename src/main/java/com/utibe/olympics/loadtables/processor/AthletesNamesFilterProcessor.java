package com.utibe.olympics.loadtables.processor;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class AthletesNamesFilterProcessor implements ItemProcessor<CsvAthleteRow, CsvAthleteRow> {

    private static final Logger logger = LoggerFactory.getLogger(AthletesNamesFilterProcessor.class);
    private final Set<CsvAthleteRow> csvAthleteRowProcessed = new HashSet<>();

    private final Set<Integer> processedIds = new HashSet<>();

    public AthletesNamesFilterProcessor() {
    }

    @Override
    public CsvAthleteRow process(final CsvAthleteRow csvAthleteRow) throws Exception {
        //logger.info("hashset of Ids is {}", Arrays.toString(processedIds.toArray()) );

        if(processedIds.contains(csvAthleteRow.getId())) {
            //logger.info("trapped duplicate for Id {}", csvAthleteRow.getId());
            return null;
        }
        processedIds.add(csvAthleteRow.getId());
        //logger.info(" Id {} not duplicated", csvAthleteRow.getId());


        return csvAthleteRow;
    }

}
