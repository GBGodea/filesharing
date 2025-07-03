package org.godea.repositories;

import jakarta.persistence.EntityManager;
import org.godea.di.Repository;
import org.godea.models.FileRecord;
import org.godea.models.dto.FileDTO;
import org.godea.models.dto.PageDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    public PageDTO<FileDTO> viewAllFiles(int limit, int offset) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            List<FileRecord> files = em.createQuery("SELECT f FROM FileRecord f" +
                            " ORDER BY f.uploadTime", FileRecord.class)
                    .setMaxResults(limit)
                    .setFirstResult(offset)
                    .getResultList();
            long count = em.createQuery("SELECT COUNT(f) FROM FileRecord f", Long.class).getSingleResult();
            System.out.println("COUNT: " + count);
            em.getTransaction().commit();

            List<FileDTO> fileDTO = files.stream()
                    .map(file -> new FileDTO(
                            file.getOriginalName(),
                            file.getSize(),
                            file.getUploadTime(),
                            file.getExpirationDate(),
                            file.getIsExpired(),
                            file.getDownloadCount()
                    )).toList();
            return new PageDTO<>(fileDTO, count);
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            e.getStackTrace();
            throw e;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public void incrementDownload(UUID id) {
        EntityManager em = EMFProvider.getFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE FileRecord f " +
                    "SET f.downloadCount = f.downloadCount + 1 " +
                    "WHERE f.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}