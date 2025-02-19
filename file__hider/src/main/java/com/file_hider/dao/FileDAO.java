package com.file_hider.dao;

import com.file_hider.models.HiddenFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class FileDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FileHiderPU");

    public void save(HiddenFile file) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(file);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public HiddenFile update(HiddenFile file) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            HiddenFile updated = em.merge(file);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    public HiddenFile findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(HiddenFile.class, id);
        } finally {
            em.close();
        }
    }

    public List<HiddenFile> findByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT f FROM HiddenFile f WHERE f.user.id = :userId", HiddenFile.class)
                     .setParameter("userId", userId)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
