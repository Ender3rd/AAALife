package com.example.aaalife.controller;

import com.example.aaalife.model.UserAccountAccess;
import com.example.aaalife.repository.UserAccountAccessRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user-account-accesses")
public class UserAccountAccessController {

    private final UserAccountAccessRepository userAccountAccessRepository;

    public UserAccountAccessController(UserAccountAccessRepository userAccountAccessRepository) {
        this.userAccountAccessRepository = userAccountAccessRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountAccess> getById(@PathVariable Long id) {
        return userAccountAccessRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}