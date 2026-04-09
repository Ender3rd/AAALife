package com.example.aaalife.controller;

import com.example.aaalife.model.Claim;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Reduces the number of API calls and hide unnecessary data we provide this shortcut.
    @GetMapping("/{id}/claims")
    public ResponseEntity<List<Claim>> getClaimsById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getClaims()))
                .orElse(ResponseEntity.notFound().build());
    }
}