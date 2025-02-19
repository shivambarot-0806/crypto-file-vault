package com.file_hider.dao;

import com.file_hider.models.KeyShare;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class KeyShareDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FileHiderPU");

    public void save(KeyShare keyShare) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(keyShare);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public KeyShare update(KeyShare keyShare) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            KeyShare updated = em.merge(keyShare);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    public List<KeyShare> findByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT k FROM KeyShare k WHERE k.user.id = :userId", KeyShare.class)
                     .setParameter("userId", userId)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
