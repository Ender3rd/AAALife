package com.example.aaalife.controller;

import com.example.aaalife.model.ClaimChange;
import com.example.aaalife.repository.ClaimChangeRepository;
import com.example.aaalife.service.ClaimChangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claim-changes")
public class ClaimChangeController {

    private final ClaimChangeService claimChangeService;
    private final ClaimChangeRepository claimChangeRepository;

    public ClaimChangeController(ClaimChangeService claimChangeService, ClaimChangeRepository claimChangeRepository) {
        this.claimChangeService = claimChangeService;
        this.claimChangeRepository = claimChangeRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimChange> getById(@PathVariable Long id) {
        return claimChangeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClaimChange> create(@RequestBody ClaimChange claimChange) {
        ClaimChange saved = claimChangeService.save(claimChange);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}