package com.example.aaalife;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.aaalife.model.Role;
import com.example.aaalife.model.User;
import com.example.aaalife.repository.UserRepository;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaAuditing
public class AaaLifeApplication {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(AaaLifeApplication.class, args);
        // TODO add more test cases for repositories and services
        // TODO add basic data initialization for testing
        // test of threaded comments.
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            if (userRepository.count() == 0) {
                userRepository.save(new User("customer", Role.Customer));
                userRepository.save(new User("adjuster", Role.Adjuster));
            }
        };
    }
}
