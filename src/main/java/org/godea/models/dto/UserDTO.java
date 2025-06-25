package org.godea.models.dto;

import java.util.UUID;

public class UserDTO {
    UUID id;
    String email;

    public UserDTO(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
