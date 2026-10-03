package br.edu.ifpb.tool_lending_system.DAO;

import br.edu.ifpb.tool_lending_system.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Category save(Category category) {
        entityManager.persist(category);
        return category;
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Category.class, id));
    }

    public Optional<Category> findByName(String name) {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT c FROM Category c WHERE c.name = :name",
                Category.class
        );

        query.setParameter("name", name);

        return query.getResultStream().findFirst();
    }

    public List<Category> listAll() {
        return entityManager
                .createQuery("SELECT c FROM Category c", Category.class)
                .getResultList();
    }

    @Transactional
    public Category update(Category category) {
        return entityManager.merge(category);
    }

    @Transactional
    public void delete(Category category) {
        entityManager.remove(
                entityManager.contains(category)
                        ? category
                        : entityManager.merge(category)
        );
    }
}

