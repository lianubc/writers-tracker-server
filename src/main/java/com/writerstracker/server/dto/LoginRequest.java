package com.writerstracker.server.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


public class LoginRequest {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "Username or Email is required")
    private String username; // We will allow users to log in with either

    @NotBlank(message = "Password is required")
    private String password;
}

