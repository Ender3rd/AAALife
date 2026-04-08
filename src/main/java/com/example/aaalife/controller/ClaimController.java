package com.example.aaalife.controller;

import com.example.aaalife.model.Claim;
import com.example.aaalife.repository.ClaimRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimRepository claimRepository;

    public ClaimController(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getById(@PathVariable Long id) {
        return claimRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Claim> create(@RequestBody Claim claim) {
        Claim saved = claimRepository.save(claim);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/duplicates/{since}/{before}")
    public ResponseEntity<List<Claim>> getDuplicatesSince(@PathVariable String since, @PathVariable String before) {
        Instant sinceTimestamp = Instant.parse(since);
        Instant beforeTimestamp = Instant.parse(before);
        return claimRepository.findDuplicatesBetween(sinceTimestamp, beforeTimestamp)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}