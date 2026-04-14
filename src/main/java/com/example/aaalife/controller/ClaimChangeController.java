package com.example.aaalife.controller;

import com.example.aaalife.model.Claim;
import com.example.aaalife.model.ClaimChange;
import com.example.aaalife.model.ClaimStatus;
import com.example.aaalife.model.Role;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.ClaimChangeRepository;
import com.example.aaalife.repository.ClaimRepository;
import com.example.aaalife.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/policies/{policyId}/claims/{claimId}/changes")
public class ClaimChangeController {

    private final ClaimChangeRepository claimChangeRepository;
    private final UserRepository userRepository;
    private final ClaimRepository claimRepository;

    public ClaimChangeController(ClaimChangeRepository claimChangeRepository, UserRepository userRepository,
            ClaimRepository claimRepository) {
        this.userRepository = userRepository;
        this.claimChangeRepository = claimChangeRepository;
        this.claimRepository = claimRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimChange> getById(@PathVariable Long id) {
        return claimChangeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClaimChange> create(@RequestBody ClaimChange claimChange, @PathVariable Long claimId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (claimChange.getUser() != null && !claimChange.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        claimChange.setUser(user);
        // TODO create a very careful validation of what roles are allowed to change the
        // status in what ways.
        // for now, hack it.
        if (ClaimStatus.Approved.equals(claimChange.getStatus()) && !Role.Adjuster.equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Claim claim = claimRepository.findById(claimId).orElse(null);
        claimChange.setClaim(claim);
        ClaimChange saved = claimChangeRepository.save(claimChange);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}