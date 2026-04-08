package com.example.aaalife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AaaLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AaaLifeApplication.class, args);
        // TODO basic auditing interceptor
        // TODO update docs on how to run and test the application
        // TODO get username from security context when creating ClaimChange records
        // TODO add more test cases for repositories and services
        // TODO add basic data initialization for testing
        // TODO integrate with log4j
        // TODO concrete demo of both CoR and eventing
        // TODO additional claims metadata?
    }
}
