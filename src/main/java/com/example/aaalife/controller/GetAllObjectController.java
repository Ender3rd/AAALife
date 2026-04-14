package com.example.aaalife.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aaalife.model.Account;
import com.example.aaalife.model.Claim;
import com.example.aaalife.model.Policy;
import com.example.aaalife.repository.ClaimRepository;
import com.example.aaalife.repository.PolicyRepository;

@RestController
@RequestMapping("/api")
public class GetAllObjectController {

    private final PolicyRepository policyRepository;
    private final AccountController accountController;
    private final ClaimRepository claimRepository;

    public GetAllObjectController(PolicyRepository policyRepository, AccountController accountController,
            ClaimRepository claimRepository) {
        this.policyRepository = policyRepository;
        this.accountController = accountController;
        this.claimRepository = claimRepository;
    }

    @GetMapping
    @RequestMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountController.getAll();
    }

    @GetMapping
    @RequestMapping("/policies")
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @GetMapping
    @RequestMapping("/claims")
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }
}
