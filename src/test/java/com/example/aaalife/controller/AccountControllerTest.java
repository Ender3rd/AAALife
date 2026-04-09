package com.example.aaalife.controller;

import com.example.aaalife.model.Account;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setName("Test Account");

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetById() throws Exception {
        Account account = new Account();
        account.setName("Test Account");
        Account saved = entityManager.persistAndFlush(account);

        mockMvc.perform(get("/api/accounts/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Account"));
    }
}
