package com.example.aaalife.repository;

import com.example.aaalife.model.Account;
import com.example.aaalife.model.Claim;
import com.example.aaalife.model.Policy;
import com.example.aaalife.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClaimRepositoryTest {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindDuplicatesBetween() {
        // Create test data
        Account account = new Account();
        account.setName("Test Account");
        account = accountRepository.save(account);

        User user = new User();
        user.setUsername("testuser");
        user.setRole(com.example.aaalife.model.Role.Adjuster);
        user = userRepository.save(user);

        Policy policy = new Policy("POL123", account);
        policy = policyRepository.save(policy);

        Instant incidentDate = Instant.parse("2023-01-01T00:00:00Z");
        Instant since = Instant.parse("2023-01-01T00:00:00Z");
        Instant before = Instant.parse("2023-01-02T00:00:00Z");

        // Create 5 claims with same policy and incidentDate
        for (int i = 0; i < 5; i++) {
            Claim claim = new Claim(policy, user);
            claim.setIncidentDate(incidentDate);
            claim.setCreatedAt(since.plusSeconds(i * 60)); // Different times within range
            claimRepository.save(claim);
        }

        // Test the query
        Optional<List<Claim>> duplicates = claimRepository.findDuplicatesBetween(since, before);
        assertThat(duplicates).isPresent();
        assertThat(duplicates.get()).hasSize(5);
    }

    @Test
    void testFindDuplicatesBetween_NoDuplicates() {
        // Similar setup but less than 5 claims
        Account account = new Account();
        account.setName("Test Account");
        account = accountRepository.save(account);

        User user = new User();
        user.setUsername("testuser2");
        user.setRole(com.example.aaalife.model.Role.Adjuster);
        user = userRepository.save(user);

        Policy policy = new Policy("POL124", account);
        policy = policyRepository.save(policy);

        Instant incidentDate = Instant.parse("2023-01-01T00:00:00Z");
        Instant since = Instant.parse("2023-01-01T00:00:00Z");
        Instant before = Instant.parse("2023-01-02T00:00:00Z");

        // Create 3 claims
        for (int i = 0; i < 3; i++) {
            Claim claim = new Claim(policy, user);
            claim.setIncidentDate(incidentDate);
            claim.setCreatedAt(since.plusSeconds(i * 60));
            claimRepository.save(claim);
        }

        Optional<List<Claim>> duplicates = claimRepository.findDuplicatesBetween(since, before);
        assertThat(duplicates).isPresent();
        assertThat(duplicates.get()).isEmpty();
    }
}
