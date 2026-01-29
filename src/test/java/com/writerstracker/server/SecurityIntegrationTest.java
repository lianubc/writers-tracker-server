package com.writerstracker.server;

import com.writerstracker.server.model.User;
import com.writerstracker.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        if (userRepository.findByEmail("test@writer.com").isEmpty()) {
            User user = new User();
            user.setEmail("test@writer.com");
            user.setUsername("TestWriter");
            user.setPassword("hashedpassword");
            userRepository.save(user);
        }
    }

    @Test
    void whenUnauthenticated_thenAccessDenied() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@writer.com")
    void whenAuthenticated_thenAccessAllowed() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }
}