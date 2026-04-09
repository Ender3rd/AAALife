package com.example.aaalife.repository;

import com.example.aaalife.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void testSaveAndFindById() {
        Document document = new Document();
        document.setFileHash("some fake hash");
        document.setFileLocation("/opt/ontap/docs/claim/17/foo.pdf");
        document.setFileSize(12l);
        document.setParentId(17l);
        document.setParentType("Claim");
        document.setRelatesToId(document.getParentId());
        document.setRelatesToType(document.getParentType());
        Document saved = documentRepository.save(document);

        assertThat(saved.getId()).isNotNull();
        assertThat(documentRepository.findById(saved.getId())).isPresent();
    }
}
