package com.example.Spring.boot.POS.System.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> roles = new HashSet<>();

    // Getters and setters
}
