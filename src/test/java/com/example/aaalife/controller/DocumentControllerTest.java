package com.example.aaalife.controller;

import com.example.aaalife.model.Document;
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
        // TODO proper testing

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetById() throws Exception {
        Document document = new Document();
        // TODO proper testing
        Document saved = entityManager.persistAndFlush(document);

        mockMvc.perform(get("/api/documents/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Document"));
    }
}
