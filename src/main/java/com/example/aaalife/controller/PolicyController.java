package com.example.aaalife.controller;

import com.example.aaalife.model.Policy;
import com.example.aaalife.repository.AccountRepository;
import com.example.aaalife.repository.PolicyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/policies")
public class PolicyController {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private AccountRepository accountRepository;

    public PolicyController(PolicyRepository policyRepository, AccountRepository accountRepository) {
        this.policyRepository = policyRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<Policy> getAll(@PathVariable Long accountId) {
        return policyRepository.findAllById(List.of(accountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getById(@PathVariable Long id) {
        return policyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Policy> create(@RequestBody Policy policy, @PathVariable Long accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    policy.setAccount(account);
                    Policy saved = policyRepository.save(policy);
                    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
