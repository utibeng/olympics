package com.utibe.olympics.output.tables.olympicevents;

import com.utibe.olympics.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class OlympicEventsFilterProcessor implements ItemProcessor<CsvAthleteRow, CsvAthleteRow> {

    private static final Logger logger = LoggerFactory.getLogger(OlympicEventsFilterProcessor.class);

    private final Set<String> processedOlympicEvents = new HashSet<>();

    public OlympicEventsFilterProcessor() {
    }

    @Override
    public CsvAthleteRow process(final CsvAthleteRow csvAthleteRow) throws Exception {
        //logger.info("hashset of Ids is {}", Arrays.toString(processedIds.toArray()) );

        String olympicEventsKey = csvAthleteRow.getEvent() + csvAthleteRow.getGames();

        if(processedOlympicEvents.contains(olympicEventsKey) ){
            //logger.info("trapped duplicate for olympicEventsKey {}", olympicEventsKey);
            return null;
        }
        processedOlympicEvents.add(olympicEventsKey);
        //logger.info(" Id {} not duplicated", csvAthleteRow.getId());

        return csvAthleteRow;
    }

}
