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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @WithUserDetails("customer")
    void testCreateDocument() throws Exception {
        Document document = new Document();
        document.setFileHash("some fake hash");
        document.setFileLocation("/opt/ontap/docs/claim/17/foo.pdf");
        document.setFileSize(12l);
        document.setParentId(17l);
        document.setParentType("Claim");
        document.setRelatesToId(document.getParentId());
        document.setRelatesToType(document.getParentType());

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("customer")
    void testGetById() throws Exception {
        Document document = new Document();
        document.setFileHash("some fake hash");
        document.setFileLocation("/opt/ontap/docs/claim/17/foo.pdf");
        document.setFileSize(12l);
        document.setParentId(17l);
        document.setParentType("Claim");
        document.setRelatesToId(document.getParentId());
        document.setRelatesToType(document.getParentType());
        Document saved = entityManager.persistAndFlush(document);

        mockMvc.perform(get("/api/documents/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileLocation").value(document.getFileLocation()));
    }
}
