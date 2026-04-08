package com.example.aaalife.controller;

import com.example.aaalife.model.UserAccountAccess;
import com.example.aaalife.repository.UserAccountAccessRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-account-accesses")
public class UserAccountAccessController {

    private final UserAccountAccessRepository userAccountAccessRepository;

    public UserAccountAccessController(UserAccountAccessRepository userAccountAccessRepository) {
        this.userAccountAccessRepository = userAccountAccessRepository;
    }

    @GetMapping
    public List<UserAccountAccess> getAll() {
        return userAccountAccessRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountAccess> getById(@PathVariable Long id) {
        return userAccountAccessRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserAccountAccess> create(@RequestBody UserAccountAccess userAccountAccess) {
        UserAccountAccess saved = userAccountAccessRepository.save(userAccountAccess);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}