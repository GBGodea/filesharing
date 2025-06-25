package org.godea.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String email;
    String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
