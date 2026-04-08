package com.example.aaalife;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.aaalife.repository.ClaimRepository;

@Component
public class DuplicateReporter {
    private static final Logger logger = LogManager.getLogger(DuplicateReporter.class);

    @Autowired
    private ClaimRepository claimRepository;

    public DuplicateReporter(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }
    
    @Scheduled(cron = "0 0 * * * ?") // Every hour
    private void reportDuplicateClaims() {
        Instant now = Instant.now();
        Instant oneHourAgo = now.minus(Duration.ofHours(1)).minus(Duration.ofSeconds(90)); // a little buffer for processing time
        claimRepository.findDuplicatesBetween(oneHourAgo, now)
                .ifPresent(duplicates -> {
                    logger.warn("Found {} duplicate claims in the last hour", duplicates.size());
                });
    }
}
