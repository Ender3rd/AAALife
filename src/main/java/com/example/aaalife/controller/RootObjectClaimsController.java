package com.example.aaalife.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aaalife.model.Claim;
import com.example.aaalife.repository.ClaimRepository;

@RestController
@RequestMapping("/api/claims")
public class RootObjectClaimsController {
    private ClaimRepository claimRepository;

    public RootObjectClaimsController(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @GetMapping("/duplicates/{since}/{before}")
    public ResponseEntity<List<Claim>> getDuplicatesSince(@PathVariable String since,
            @PathVariable(required = false) String before) {
        Instant sinceTimestamp = Instant.parse(since);
        Instant beforeTimestamp = Instant.parse(before);
        List<Claim> duplicates = claimRepository.findDuplicatesBetween(sinceTimestamp, beforeTimestamp);
        return duplicates.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(duplicates);
    }
}