package br.edu.ifpb.tool_lending_system.dao;

import br.edu.ifpb.tool_lending_system.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class CategoryDAO {

    private final EntityManagerFactory emf;

    public CategoryDAO() {
        this.emf = Persistence.createEntityManagerFactory("tool-lending");
    }

    public void salvar(Category category) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(category);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public Category buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Category.class, id);

        } finally {
            em.close();
        }
    }

    public List<Category> listarTodos() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category c",
                    Category.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    public boolean existePorNome(String nome) {
        EntityManager em = emf.createEntityManager();

        try {
            Long quantidade = em.createQuery(
                            "SELECT COUNT(c) FROM Category c " +
                                    "WHERE LOWER(c.name) = LOWER(:nome)",
                            Long.class
                    )
                    .setParameter("nome", nome)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            em.close();
        }
    }

    public boolean existePorNomeEIdDiferente(String nome, Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            Long quantidade = em.createQuery(
                            "SELECT COUNT(c) FROM Category c " +
                                    "WHERE LOWER(c.name) = LOWER(:nome) " +
                                    "AND c.id <> :id",
                            Long.class
                    )
                    .setParameter("nome", nome)
                    .setParameter("id", id)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            em.close();
        }
    }

    public Category atualizar(Category category) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Category atualizada = em.merge(category);

            em.getTransaction().commit();

            return atualizada;

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Category category = em.find(Category.class, id);

            if (category != null) {
                em.remove(category);
            }

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }
}