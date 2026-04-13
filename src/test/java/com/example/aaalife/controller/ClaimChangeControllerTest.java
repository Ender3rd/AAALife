package com.example.aaalife.controller;

import com.example.aaalife.model.*;
import com.example.aaalife.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class ClaimChangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails("customer")
    void testCreateClaimChange() throws Exception {
        User user = userRepository.findByUsername("customer").orElseThrow();

        // Create dependencies
        Account account = new Account();
        account.setName("Test Account");
        account = entityManager.persistAndFlush(account);

        Policy policy = new Policy("POL123", account);
        policy = entityManager.persistAndFlush(policy);

        Claim claim = new Claim(policy, user);
        claim.setIncidentDate(java.time.Instant.now());
        claim = entityManager.persistAndFlush(claim);

        ClaimChange claimChange = new ClaimChange(claim, user, ClaimStatus.Reviewable);

        mockMvc.perform(post("/api/claim-changes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimChange)))
                .andExpect(status().isCreated());
    }

    // TODO test limits on who can approve claims.

    @Test
    @WithUserDetails("customer")
    void testGetById() throws Exception {
        // Create a claim change
        Account account = new Account();
        account.setName("Test Account");
        account = entityManager.persistAndFlush(account);

        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.Adjuster);
        user = entityManager.persistAndFlush(user);

        Policy policy = new Policy("POL123", account);
        policy = entityManager.persistAndFlush(policy);

        Claim claim = new Claim(policy, user);
        claim.setIncidentDate(java.time.Instant.now());
        claim = entityManager.persistAndFlush(claim);

        ClaimChange claimChange = new ClaimChange(claim, user, ClaimStatus.Reviewable);
        ClaimChange saved = entityManager.persistAndFlush(claimChange);

        mockMvc.perform(get("/api/claim-changes/{id}", saved.getId()))
                .andExpect(status().isOk());
    }
}
