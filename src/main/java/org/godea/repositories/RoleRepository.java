package org.godea.repositories;

import jakarta.persistence.EntityManager;
import org.godea.di.Repository;
import org.godea.models.Role;
import org.godea.models.enums.Roles;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepository extends JPArepository<Role, UUID> {
    public Optional<Role> findRoleByName(String name) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();

        Roles enRole = Roles.valueOf(name);
        Role role = em.createQuery("SELECT r" +
                " FROM Role r WHERE r.role = :enRole", Role.class)
                .setParameter("enRole", enRole).getSingleResult();

        return Optional.of(role);
    }

    public Optional<Role> findRoleById(UUID id) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();

        Role role = em.createQuery("SELECT r" +
                        " FROM Role r WHERE r.id = :id", Role.class)
                .setParameter("id", id).getSingleResult();

        return Optional.of(role);
    }
}
