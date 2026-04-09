package com.example.aaalife.controller;

import com.example.aaalife.model.Claim;
import com.example.aaalife.model.ClaimChange;
import com.example.aaalife.model.ClaimStatus;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.ClaimRepository;
import com.example.aaalife.repository.UserRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;

    public ClaimController(ClaimRepository claimRepository, UserRepository userRepository) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getById(@PathVariable Long id) {
        return claimRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Claim> create(@RequestBody Claim claim) {
        if (claim.getClaimChanges() != null && !claim.getClaimChanges().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (claim.getUser() != null && !claim.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ClaimChange claimChange = new ClaimChange(claim, user, ClaimStatus.Created);
        claim.setClaimChanges(List.of(claimChange));
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