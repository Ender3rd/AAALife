package com.example.aaalife.repository;

import com.example.aaalife.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClaimChangeRepositoryTest {

    @Autowired
    private ClaimChangeRepository claimChangeRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAndFindById() {
        // Create dependencies
        Account account = new Account();
        account.setName("Test Account");
        account = accountRepository.save(account);

        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.Adjuster);
        user = userRepository.save(user);

        Policy policy = new Policy("POL123", account);
        policy = policyRepository.save(policy);

        Claim claim = new Claim(policy, user);
        claim.setIncidentDate(java.time.Instant.now());
        claim = claimRepository.save(claim);

        ClaimChange claimChange = new ClaimChange(claim, user, ClaimStatus.Submitted);
        ClaimChange saved = claimChangeRepository.save(claimChange);

        assertThat(saved.getId()).isNotNull();
        assertThat(claimChangeRepository.findById(saved.getId())).isPresent();
    }
}