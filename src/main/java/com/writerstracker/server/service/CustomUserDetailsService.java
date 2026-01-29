package com.writerstracker.server.service;

import com.writerstracker.server.model.User;
import com.writerstracker.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Find the user in YOUR database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Convert it to a Spring Security "User" object
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),       // Use Email as the username for login
                user.getPassword(),    // The hashed password from DB
                Collections.emptyList() // Authorities (Roles) - empty for now
        );
    }
}

