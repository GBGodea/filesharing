package org.godea.repositories;

import jakarta.persistence.EntityManager;
import org.godea.di.Repository;
import org.godea.models.FileRecord;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FileRepository {
    public FileRecord save(FileRecord record) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(record);
            em.getTransaction().commit();
            return record;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<FileRecord> findById(UUID id) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        try {
            FileRecord fileRecord = em.find(FileRecord.class, id);
            return Optional.ofNullable(fileRecord);
        } finally {
            em.close();
        }
    }

    public void updateIsExpired(boolean updateTo, UUID id) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE FileRecord f SET f.isExpired = :updateTo" +
                    " WHERE f.id = :id")
                    .setParameter("updateTo", updateTo)
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            em.close();
        }
    }
}
