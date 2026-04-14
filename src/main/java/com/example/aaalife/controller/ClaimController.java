package com.example.aaalife.controller;

import com.example.aaalife.model.Claim;
import com.example.aaalife.model.ClaimChange;
import com.example.aaalife.model.ClaimStatus;
import com.example.aaalife.model.Policy;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.ClaimRepository;
import com.example.aaalife.repository.PolicyRepository;
import com.example.aaalife.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/policies/{policyId}/claims")
public class ClaimController {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;

    public ClaimController(ClaimRepository claimRepository, UserRepository userRepository,
            PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getById(@PathVariable Long id) {
        return claimRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<List<Claim>> getAll(@PathVariable Long accountId, @PathVariable Long policyId) {
        return ResponseEntity.ok(claimRepository.findAllByPolicyId(policyId));
    }

    @PostMapping
    public ResponseEntity<Claim> create(@RequestBody Claim claim, @PathVariable Long accountId,
            @PathVariable Long policyId) {
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

        Optional<Policy> policyHuh = policyRepository.findById(policyId);
        if (policyHuh.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        policyHuh.ifPresent(claim::setPolicy);

        ClaimChange claimChange = new ClaimChange(claim, user, ClaimStatus.Created);
        claim.setClaimChanges(List.of(claimChange));
        claim.setUser(user);
        Claim saved = claimRepository.save(claim);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
