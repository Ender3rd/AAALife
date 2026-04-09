package com.example.aaalife.repository;

import com.example.aaalife.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void testSaveAndFindById() {
        Note note = new Note();
        note.setContent("Test note");
        note.setParentId(17l);
        note.setParentType("Claim");
        note.setRelatesToId(note.getParentId());
        note.setRelatesToType(note.getParentType());
        Note saved = noteRepository.save(note);

        assertThat(saved.getId()).isNotNull();
        assertThat(noteRepository.findById(saved.getId())).isPresent();
    }
}
