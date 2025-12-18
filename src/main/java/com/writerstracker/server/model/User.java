package com.writerstracker.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "pen_name")
    private String penName;

    // Constructors
    public User() {}

    public User(String username, String email, String password, String penName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.penName = penName;
    }

    // --- MANUAL GETTERS AND SETTERS (To fix the error) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password; // This fixes the "cannot find symbol" error
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPenName() {
        return penName;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }
}