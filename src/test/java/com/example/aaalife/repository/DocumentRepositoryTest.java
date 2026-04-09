package com.example.aaalife.repository;

import com.example.aaalife.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void testSaveAndFindById() {
        Document document = new Document();
        document.setName("Test Document");
        document.setContent("Test content".getBytes());
        Document saved = documentRepository.save(document);

        assertThat(saved.getId()).isNotNull();
        assertThat(documentRepository.findById(saved.getId())).isPresent();
    }
}
