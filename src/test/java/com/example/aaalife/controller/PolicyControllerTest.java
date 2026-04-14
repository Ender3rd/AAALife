package com.example.aaalife.controller;

import com.example.aaalife.model.Account;
import com.example.aaalife.model.Policy;
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
class PolicyControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private TestEntityManager entityManager;

        @Test
        @WithUserDetails("customer")
        void testCreatePolicy() throws Exception {
                Account account = new Account();
                account.setName("Test Account");
                account = entityManager.persistAndFlush(account);

                Policy policy = new Policy("POL123", null);

                mockMvc.perform(post("/api/accounts/{accountId}/policies", account.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(policy)))
                                .andExpect(status().isCreated());
        }

        @Test
        @WithUserDetails("customer")
        void testGetById() throws Exception {
                Account account = new Account();
                account.setName("Test Account");
                account = entityManager.persistAndFlush(account);

                Policy policy = new Policy("POL123", account);
                Policy saved = entityManager.persistAndFlush(policy);

                mockMvc.perform(get("/api/accounts/{accountId}/policies/{id}", account.getId(),
                                saved.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.publicId").value("POL123"));
        }

        @Test
        @WithUserDetails("customer")
        void testGetAccountAll() throws Exception {
                Account account = new Account();
                account.setName("Test Account");
                account = entityManager.persistAndFlush(account);

                mockMvc.perform(get("/api/accounts/{accountId}/policies", account.getId()))
                                .andExpect(status().isOk());
        }

        @Test
        @WithUserDetails("customer")
        void testGetAll() throws Exception {
                mockMvc.perform(get("/api/policies"))
                                .andExpect(status().isOk());
        }
}
