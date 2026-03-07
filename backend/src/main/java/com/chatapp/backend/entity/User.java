package com.chatapp.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profileImage;

    private Boolean isVerified = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email)
    {
        this.email=email;
    }

    public void setPassword(String pass)
    {
        this.password=pass;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUsername()
    {
        return username;
    }

    public String getEmail() { return email; }

}