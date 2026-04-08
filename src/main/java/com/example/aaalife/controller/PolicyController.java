package com.example.aaalife.controller;

import com.example.aaalife.model.Policy;
import com.example.aaalife.repository.PolicyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRepository policyRepository;

    public PolicyController(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @GetMapping
    public List<Policy> getAll() {
        return policyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getById(@PathVariable Long id) {
        return policyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Policy> create(@RequestBody Policy policy) {
        Policy saved = policyRepository.save(policy);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}