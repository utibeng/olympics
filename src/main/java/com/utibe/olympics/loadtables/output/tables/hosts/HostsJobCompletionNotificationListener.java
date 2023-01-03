package com.utibe.olympics.loadtables.output.tables.hosts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class HostsJobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(
            HostsJobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HostsJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! Hosts JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT games, year, season, city FROM olympics_hosts",
                    (rs, row) -> new Hosts(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4))

            ).forEach(hosts -> log.info("Found <" + hosts + "> in the database."));
        }
    }
}
