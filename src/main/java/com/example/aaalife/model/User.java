package com.example.aaalife.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(indexes = {
        @Index(name = "idx_user_role", columnList = "role"),
        @Index(name = "idx_user_username", columnList = "username")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_username", columnNames = "username")
}, name = "users_table" // "user" is a reserved keyword in some databases, so we specify the table name
                        // explicitly to avoid issues.
)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false, updatable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // cleaner and less auditing issues if internal customers stay separate.

    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<ClaimChange> claimChanges;

    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<Claim> claims;

    public User() {
    }

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ClaimChange> getClaimChanges() {
        return claimChanges;
    }

    public void setClaimChanges(List<ClaimChange> claimChanges) {
        this.claimChanges = claimChanges;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }
}
