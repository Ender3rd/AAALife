package com.example.aaalife.model;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.aaalife.service.ContentService;

import jakarta.persistence.*;

@Entity
@Table(indexes = {
    @Index(name = "idx_document_parent", columnList = "parentId, parentType"),
    @Index(name = "idx_document_relates_to", columnList = "relatesToId, relatesToType")
})
@EntityListeners(AuditingEntityListener.class)
public class Document {

    @Autowired
    public static ContentService contentService;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false, updatable = false)
    private Long parentId;

    @Column(nullable = false, updatable = false)
    private String parentType;

    @Column(nullable = false, updatable = false)
    private Long relatesToId;

    @Column(nullable = false)
    private String relatesToType;

    @Column(nullable = false, updatable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String fileLocation;

    @Column(nullable = false, updatable = false)
    private String fileHash;

    public Document() {}

    public Document(Long parentId, String parentType, Long relatesToId, String relatesToType, Long fileSize, String fileLocation, String fileHash) {
        this.parentId = parentId;
        this.parentType = parentType;
        this.relatesToId = relatesToId;
        this.relatesToType = relatesToType;
        this.fileSize = fileSize;
        this.fileLocation = fileLocation;
        this.fileHash = fileHash;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getParentType() { return parentType; }
    public void setParentType(String parentType) { this.parentType = parentType; }

    public Long getRelatesToId() { return relatesToId; }
    public void setRelatesToId(Long relatesToId) { this.relatesToId = relatesToId; }

    public String getRelatesToType() { return relatesToType; }
    public void setRelatesToType(String relatesToType) { this.relatesToType = relatesToType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getFileLocation() { return fileLocation; }
    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }

    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }

    public String getContent() {
        return ContentService.getContent(fileLocation);
    }
}