package org.godea.repositories;

import jakarta.persistence.EntityManagerFactory;

public class EMFProvider {
    private static EntityManagerFactory emf;

    public static void setFactory(EntityManagerFactory entityManagerFactory) {
        emf = entityManagerFactory;
    }

    public static EntityManagerFactory getFactory() {
        if(emf == null) {
            throw new RuntimeException("Error during creation of Entity Manager Factory");
        }
        return emf;
    }
}
