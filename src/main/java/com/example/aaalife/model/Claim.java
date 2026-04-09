package com.example.aaalife.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(indexes = {
        @Index(name = "idx_claim_policy", columnList = "policy_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false, updatable = false)
    private Policy policy;

    @OneToMany(mappedBy = "claim", orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<ClaimChange> claimChanges;

    @Column(nullable = false)
    private Instant incidentDate;

    public Claim() {
    }

    public Claim(Policy policy, User user) {
        this.policy = policy;
        this.user = user;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public List<ClaimChange> getClaimChanges() {
        return claimChanges;
    }

    public void setClaimChanges(List<ClaimChange> claimChanges) {
        this.claimChanges = claimChanges;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Instant incidentDate) {
        this.incidentDate = incidentDate;
    }
}
