package com.writerstracker.server;


import com.writerstracker.server.dto.RegisterRequest;
import com.writerstracker.server.model.User;
import com.writerstracker.server.repository.UserRepository;
import com.writerstracker.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldHashPasswordAndSave() {
        // 1. Setup Data
        RegisterRequest request = new RegisterRequest();
        request.setUsername("UnitTester");
        request.setPassword("plainPassword");
        request.setEmail("unit@test.com");

        // 2. Mock behavior (Pretend the encoder works)
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashed_secret");

        // 3. Run the method
        userService.registerUser(request);

        // 4. Verify the result (Did it save the USER with the HASHED password?)
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("plainPassword");
    }
}