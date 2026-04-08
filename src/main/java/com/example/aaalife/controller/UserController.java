package com.example.aaalife.controller;

import com.example.aaalife.model.Claim;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.ClaimRepository;
import com.example.aaalife.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final ClaimRepository claimRepository;

    public UserController(UserRepository userRepository, ClaimRepository claimRepository) {
        this.userRepository = userRepository;
        this.claimRepository = claimRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Reduces the number of API calls and hide unnecessary data we provide this shortcut.
    @GetMapping("/{id}/claims/{since}")
    public ResponseEntity<List<Claim>> getClaimsById(@PathVariable Long id, @PathVariable String since) {
        Instant timestamp = Instant.parse(since);
        return claimRepository.findByUserAndCreatedGreaterThan(id, timestamp)
                .map(claimChanges -> ResponseEntity.ok(claimChanges))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}