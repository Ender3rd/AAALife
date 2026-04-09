package com.example.aaalife.controller;

import com.example.aaalife.model.Account;
import com.example.aaalife.model.Claim;
import com.example.aaalife.repository.AccountRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Not really needed, since the account will have the claims via policies, but
    // to reduce the number of API calls and hide unnecessary data we provide this
    // shortcut.
    // No before/after since we hope that no single account will have so many claims
    // that time slices or pagination will be needed.
    @GetMapping("/{id}/claims")
    public ResponseEntity<List<Claim>> getClaimsById(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(account -> ResponseEntity.ok(account.getPolicies().stream()
                        .flatMap(policy -> policy.getClaims().stream())
                        .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        Account saved = accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
