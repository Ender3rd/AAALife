package com.example.aaalife.controller;

import com.example.aaalife.model.*;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClaimChangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @WithMockUser(username = "testuser")
    void testCreateClaimChange() throws Exception {
        // Create dependencies
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

        mockMvc.perform(post("/api/claim-changes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimChange)))
                .andExpect(status().isCreated());
    }

    @Test
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
