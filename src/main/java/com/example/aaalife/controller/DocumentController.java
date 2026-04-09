package com.example.aaalife.controller;

import com.example.aaalife.model.Document;
import com.example.aaalife.repository.DocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;

    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@PathVariable Long id) {
        return documentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Document> create(@RequestBody Document document) {
        Document saved = documentRepository.save(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
