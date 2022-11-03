package com.utibe.olympics.output.tables.countries;

import com.utibe.olympics.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class CountriesFilterProcessor implements ItemProcessor<CsvAthleteRow, CsvAthleteRow> {

    private static final Logger logger = LoggerFactory.getLogger(CountriesFilterProcessor.class);

    private final Set<String> processedCountries = new HashSet<>();

    public CountriesFilterProcessor() {
    }

    @Override
    public CsvAthleteRow process(final CsvAthleteRow csvAthleteRow) throws Exception {

        String countriesKey = csvAthleteRow.getNoc() ;

        if(processedCountries.contains(countriesKey) ){
            //logger.info("trapped duplicate for CountriesKey {}", countriesKey);
            return null;
        }
        processedCountries.add(countriesKey);

        return csvAthleteRow;
    }

}
