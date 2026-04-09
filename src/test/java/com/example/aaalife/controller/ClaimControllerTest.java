package com.example.aaalife.controller;

import java.time.Instant;
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

import com.example.aaalife.model.*;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestEntityManager
@AutoConfigureMockMvc
@Transactional
class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @WithUserDetails("customer")
    void testCreateClaim() throws Exception {
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
        claim.setIncidentDate(Instant.parse("2023-01-01T00:00:00Z"));

        mockMvc.perform(post("/api/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("customer")
    void testGetById() throws Exception {
        // Create a claim
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
        claim.setIncidentDate(Instant.parse("2023-01-01T00:00:00Z"));
        Claim saved = entityManager.persistAndFlush(claim);

        mockMvc.perform(get("/api/claims/{id}", saved.getId()))
                .andExpect(status().isOk());
    }
}
