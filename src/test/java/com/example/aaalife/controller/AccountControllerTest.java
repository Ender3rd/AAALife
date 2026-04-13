package com.example.aaalife.controller;

import com.example.aaalife.model.Account;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("customer")
    void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setName("Test Account");

        mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(account.getName()));
    }

    @Test
    @WithUserDetails("customer")
    void testGetById() throws Exception {
        Account account = new Account();
        account.setName("Test Account");
        Account saved = entityManager.persistAndFlush(account);

        mockMvc.perform(get("/api/accounts/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Account"));
    }
}
