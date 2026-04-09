package com.example.aaalife.model;

import jakarta.persistence.*;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// immutable record of a change to a claim, used for auditing and tracking claim history. Each time a claim is created or its status changes, a new ClaimChange record is created.
@Entity
@Table(indexes = {
    @Index(name = "idx_claim_change_claim", columnList = "claim_id"),
    @Index(name = "idx_claim_change_user", columnList = "user_id"),
    @Index(name = "idx_claim_change_created_at", columnList = "createdAt"),
    @Index(name = "idx_claim_change_status_by_time", columnList = "createdAt, status")
})
@EntityListeners(AuditingEntityListener.class)
public class ClaimChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false, updatable = false)
    private Claim claim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ClaimStatus status;

    @Column(nullable = true, updatable = false) // optional field to record the claim amount at the time of the change, useful for tracking changes in claim amounts over time
    private float amount;

    public ClaimChange() {}

    public ClaimChange(Claim claim, User user, ClaimStatus status) {
        this.claim = claim;
        this.user = user;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Claim getClaim() { return claim; }
    public void setClaim(Claim claim) { this.claim = claim; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }
}