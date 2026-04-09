package com.example.aaalife;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.aaalife.model.Claim;
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
        // a little buffer for processing time
        Instant oneHourAgo = now.minus(Duration.ofHours(1)).minus(Duration.ofSeconds(90));
        List<Claim> duplicates = claimRepository.findDuplicatesBetween(oneHourAgo, now);
        logger.warn("Found {} duplicate claims in the last hour", duplicates.size());
    }
}
