package com.file_hider.dao;

import com.file_hider.models.CloudStorage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class CloudStorageDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FileHiderPU");

    public void save(CloudStorage cloudStorage) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cloudStorage);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public CloudStorage update(CloudStorage cloudStorage) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CloudStorage updated = em.merge(cloudStorage);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    public List<CloudStorage> findByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM CloudStorage c WHERE c.user.id = :userId", CloudStorage.class)
                     .setParameter("userId", userId)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
