package com.example.aaalife.repository;

import com.example.aaalife.model.Account;
import com.example.aaalife.model.Policy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PolicyRepositoryTest {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAndFindById() {
        Account account = new Account();
        account.setName("Test Account");
        account = accountRepository.save(account);

        Policy policy = new Policy("POL123", account);
        Policy saved = policyRepository.save(policy);

        assertThat(saved.getId()).isNotNull();
        assertThat(policyRepository.findById(saved.getId())).isPresent();
    }
}