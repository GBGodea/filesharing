package org.godea.models.dto;

import org.godea.models.Role;

import java.util.UUID;

public class UserDTO {
    UUID id;
    String email;

    String role;

    public UserDTO(UUID id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
