package com.file_hider.dao;

import com.file_hider.models.FileLog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class FileLogDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FileHiderPU");

    public void save(FileLog fileLog) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(fileLog);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<FileLog> findByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM FileLog l WHERE l.user.id = :userId", FileLog.class)
                     .setParameter("userId", userId)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
