package com.writerstracker.server.service;

import com.writerstracker.server.dto.RegisterRequest;
import com.writerstracker.server.model.User;
import com.writerstracker.server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // Import
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Add this

    // Constructor Injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {
        // Check if email exists (Validation Logic)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPenName(request.getPenName());

        // CRITICAL: HASH THE PASSWORD BEFORE SAVING
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    // Keep your authenticate method for later, we will update it soon
}
