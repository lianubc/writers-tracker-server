package com.writerstracker.server.controller;

import com.writerstracker.server.dto.LoginRequest;
import com.writerstracker.server.dto.RegisterRequest;
import com.writerstracker.server.model.User;
import com.writerstracker.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User newUser = userService.registerUser(request);
            logger.info("New user registered: {}", newUser.getUsername()); // Log success
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            // 1. Create a "token" with the username/password submitted
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            // 2. Ask Spring Security to verify it against the database (Checks hash)
            Authentication authentication = authenticationManager.authenticate(token);

            // 3. If successful, store the user in the Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. Manually force the session to start so the Cookie is sent back
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // 5. Secure Logging (Never log the password!)
            logger.info("User logged in successfully: {}", request.getUsername());

            return ResponseEntity.ok("Login successful! Session created.");

        } catch (Exception e) {
            // 6. Secure Logging for failure (Log who tried, but not the credentials)
            logger.warn("Failed login attempt for user: {}", request.getUsername());

            // Return generic error message to avoid leaking details
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request) {
            // 1. Get the current session if it exists (false means "don't create a new one")
            HttpSession session = request.getSession(false);

            // 2. If a session exists, destroy it
            if (session != null) {
                session.invalidate();
            }

            // 3. Clear the SecurityContext (removes the "User" object from memory)
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok("Logged out successfully");
        }
    }

