package com.example.aaalife.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.aaalife.model.User;
import com.example.aaalife.repository.UserRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    // TODO - Implement proper authentication and authorization logic
    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsManager users() {
        return new UserDetailsManager() {
            private UserDetails toDetails(User user) {
                return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                    .password("") // no password for demonstration purposes
                    .authorities(user.getRole().name())
                    .build();
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
                return toDetails(user);
            }

            @Override
            public void createUser(UserDetails user) {
                userRepository.save(new User(user.getUsername(), com.example.aaalife.model.Role.valueOf(user.getAuthorities().iterator().next().getAuthority())));
            }

            @Override
            public void updateUser(UserDetails user) {
                userRepository.save(new User(user.getUsername(), com.example.aaalife.model.Role.valueOf(user.getAuthorities().iterator().next().getAuthority())));
            }

            @Override
            public void deleteUser(String username) {
                userRepository.delete(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
            }

            @Override
            public boolean userExists(String username) {
                return userRepository.findByUsername(username).isPresent();
            }
            
        };
    }
}