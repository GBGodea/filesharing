package org.godea.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.godea.di.Repository;
import org.godea.models.User;

import java.util.UUID;

@Repository
public class UserRepository extends JPArepository<User, UUID> {

    @Override
    public User save(User user) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        em.getTransaction().begin();
        System.out.println("STARTED");
        try {
            em.persist(user);
            System.out.println("ENDED");
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();            // <-- здесь вы увидите реальную причину
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
        return user;
    }
}
