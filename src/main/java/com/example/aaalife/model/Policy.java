package com.example.aaalife.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(indexes = {
    @Index(name = "idx_policy_public_id", columnList = "publicId"),
    @Index(name = "idx_policy_account", columnList = "account_id")
})
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    public Policy() {}

    public Policy(String publicId, Account account) {
        this.publicId = publicId;
        this.account = account;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public List<Claim> getClaims() { return claims; }
    public void setClaims(List<Claim> claims) { this.claims = claims; }
}