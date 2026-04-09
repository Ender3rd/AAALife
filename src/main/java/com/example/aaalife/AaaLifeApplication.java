package com.example.aaalife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class AaaLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AaaLifeApplication.class, args);
        // TODO add more test cases for repositories and services
        // TODO add basic data initialization for testing
        // test of threaded comments.
    }
}
