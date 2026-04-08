package com.example.aaalife.service;

import com.example.aaalife.model.ClaimChange;
import com.example.aaalife.repository.ClaimChangeRepository;
import com.example.aaalife.repository.ClaimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimChangeService {

    private final ClaimChangeRepository claimChangeRepository;
    private final ClaimRepository claimRepository;

    public ClaimChangeService(ClaimChangeRepository claimChangeRepository, ClaimRepository claimRepository) {
        this.claimChangeRepository = claimChangeRepository;
        this.claimRepository = claimRepository;
    }

    @Transactional
    public ClaimChange save(ClaimChange claimChange) {
        ClaimChange saved = claimChangeRepository.save(claimChange);
        // Update the claim's status to the latest
        claimChange.getClaim().setStatus(saved.getStatus());
        claimRepository.save(claimChange.getClaim());
        // TODO: fail the claim change if there might be a concurrent update issue
        return saved;
    }
}