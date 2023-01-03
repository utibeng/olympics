package com.utibe.olympics.loadtables.output.tables.hosts;

import com.utibe.olympics.loadtables.input.CsvAthleteRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class HostsFilterProcessor implements ItemProcessor<CsvAthleteRow, CsvAthleteRow> {

    private static final Logger logger = LoggerFactory.getLogger(HostsFilterProcessor.class);

    private final Set<String> processedHosts = new HashSet<>();

    public HostsFilterProcessor() {
    }

    @Override
    public CsvAthleteRow process(final CsvAthleteRow csvAthleteRow) throws Exception {

        String hostsKey = csvAthleteRow.getGames();

        if(processedHosts.contains(hostsKey) ){
            //logger.info("trapped duplicate for hostsKey {}", hostsKey);
            return null;
        }
        processedHosts.add(hostsKey);


        return csvAthleteRow;
    }

}
