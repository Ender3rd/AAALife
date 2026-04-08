package com.example.aaalife.model;

import jakarta.persistence.*;

@Entity
@Table(indexes = {
    @Index(name = "idx_note_parent", columnList = "parentId, parentType"),
    @Index(name = "idx_note_relates_to", columnList = "relatesToId, relatesToType")
})
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.TIMESTAMP)
    private Instant createdAt;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private String parentType;

    @Column(nullable = false)
    private Long relatesToId;

    @Column(nullable = false)
    private String relatesToType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public Note() {}

    public Note(Long parentId, String parentType, Long relatesToId, String relatesToType, String content) {
        this.parentId = parentId;
        this.parentType = parentType;
        this.relatesToId = relatesToId;
        this.relatesToType = relatesToType;
        this.content = content;
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

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}