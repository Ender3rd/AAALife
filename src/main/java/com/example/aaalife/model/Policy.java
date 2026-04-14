package com.example.aaalife.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.OptBoolean;

@Entity
@Table(indexes = {
        @Index(name = "idx_policy_public_id", columnList = "publicId"),
        @Index(name = "idx_policy_account", columnList = "account_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Policy {
    // TODO policy limits (including verification of claims against limits)
    // TODO a way to model expired/cancelled policies from lack of payments, etc.

    @Id
    @JacksonInject(optional = OptBoolean.TRUE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    @JsonBackReference(value = "account-policies")
    private Account account;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "policy-claims")
    private List<Claim> claims;

    public Policy() {
    }

    public Policy(String publicId, Account account) {
        this.publicId = publicId;
        this.account = account;
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

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }
}