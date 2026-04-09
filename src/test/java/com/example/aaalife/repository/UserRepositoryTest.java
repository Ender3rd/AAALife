package com.example.aaalife.repository;

import com.example.aaalife.model.Role;
import com.example.aaalife.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.Adjuster);
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.Adjuster);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }
}
