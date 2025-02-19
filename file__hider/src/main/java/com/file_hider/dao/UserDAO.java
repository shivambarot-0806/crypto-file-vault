package com.file_hider.dao;

import com.file_hider.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.NoResultException;

public class UserDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FileHiderPU");

    public void save(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public User update(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User updated = em.merge(user);
            em.getTransaction().commit();
            return updated;
        } finally {
            em.close();
        }
    }

    public User findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public User findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
