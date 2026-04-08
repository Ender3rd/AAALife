package com.example.aaalife.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_account_access", indexes = {
    @Index(name = "idx_user_account_access_user", columnList = "user_id"),
    @Index(name = "idx_user_account_access_account", columnList = "account_id"),
    @Index(name = "idx_user_account_access_unique", columnList = "user_id, account_id", unique = true)
})
public class UserAccountAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public UserAccountAccess() {}

    public UserAccountAccess(User user, Account account) {
        this.user = user;
        this.account = account;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}