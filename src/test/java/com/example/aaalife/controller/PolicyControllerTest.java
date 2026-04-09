package com.example.aaalife.controller;

import com.example.aaalife.model.Account;
import com.example.aaalife.model.Policy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreatePolicy() throws Exception {
        Account account = new Account();
        account.setName("Test Account");
        account = entityManager.persistAndFlush(account);

        Policy policy = new Policy("POL123", account);

        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(policy)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetById() throws Exception {
        Account account = new Account();
        account.setName("Test Account");
        account = entityManager.persistAndFlush(account);

        Policy policy = new Policy("POL123", account);
        Policy saved = entityManager.persistAndFlush(policy);

        mockMvc.perform(get("/api/policies/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").value("POL123"));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk());
    }
}