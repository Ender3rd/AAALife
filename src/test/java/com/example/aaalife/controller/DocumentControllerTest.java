package com.example.aaalife.controller;

import com.example.aaalife.model.Document;
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
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateDocument() throws Exception {
        Document document = new Document();
        document.setName("Test Document");
        document.setContent("Test content".getBytes());

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetById() throws Exception {
        Document document = new Document();
        document.setName("Test Document");
        document.setContent("Test content".getBytes());
        Document saved = entityManager.persistAndFlush(document);

        mockMvc.perform(get("/api/documents/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Document"));
    }
}