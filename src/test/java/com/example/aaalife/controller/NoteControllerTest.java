package com.example.aaalife.controller;

import com.example.aaalife.model.Note;
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
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @WithUserDetails("customer")
    void testCreateNote() throws Exception {
        Note note = new Note();
        note.setContent("Test note");
        note.setParentId(17l);
        note.setParentType("Claim");
        note.setRelatesToId(note.getParentId());
        note.setRelatesToType(note.getParentType());

        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("customer")
    void testGetById() throws Exception {
        Note note = new Note();
        note.setContent("Test note");
        note.setParentId(17l);
        note.setParentType("Claim");
        note.setRelatesToId(note.getParentId());
        note.setRelatesToType(note.getParentType());
        Note saved = entityManager.persistAndFlush(note);

        mockMvc.perform(get("/api/notes/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test note"));
    }
}
