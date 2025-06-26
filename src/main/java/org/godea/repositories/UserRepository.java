package org.godea.repositories;

import jakarta.persistence.EntityManager;
import org.godea.di.Repository;
import org.godea.models.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository extends JPArepository<User, UUID> {

    @Override
    public User save(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }

        EntityManager em = EMFProvider.getFactory().createEntityManager();
        em.getTransaction().begin();
        System.out.println("STARTED");
        try {
            em.persist(user);
            System.out.println("ENDED");
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();

        return Optional.of(user);
    }

    public boolean existsByEmail(String email) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        em.close();
        return count > 0;
    }
}
