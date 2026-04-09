package com.example.aaalife.repository;

import com.example.aaalife.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAndFindById() {
        Account account = new Account();
        account.setName("Test Account");
        Account saved = accountRepository.save(account);

        assertThat(saved.getId()).isNotNull();
        assertThat(accountRepository.findById(saved.getId())).isPresent();
    }
}