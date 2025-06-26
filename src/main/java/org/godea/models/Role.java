package org.godea.models;

import jakarta.persistence.*;
import org.godea.models.enums.Roles;

import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Roles role;

    public Roles getRole() {
        return role;
    }
}
