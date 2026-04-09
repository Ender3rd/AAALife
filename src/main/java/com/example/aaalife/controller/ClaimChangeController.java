package com.example.aaalife.controller;

import com.example.aaalife.model.ClaimChange;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.ClaimChangeRepository;
import com.example.aaalife.repository.UserRepository;
import com.example.aaalife.service.ClaimChangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claim-changes")
public class ClaimChangeController {

    private final ClaimChangeService claimChangeService;
    private final ClaimChangeRepository claimChangeRepository;
    private final UserRepository userRepository;

    public ClaimChangeController(ClaimChangeService claimChangeService, ClaimChangeRepository claimChangeRepository, UserRepository userRepository) {
        this.userRepository = userRepository;  
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (claimChange.getUser() != null && !claimChange.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        claimChange.setUser(user);
        // TODO create a very careful validation of what roles are allowed to change the status in what ways.
        ClaimChange saved = claimChangeService.save(claimChange);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}