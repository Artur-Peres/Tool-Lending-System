package br.edu.ifpb.tool_lending_system.DAO;

import br.edu.ifpb.tool_lending_system.model.Equipment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EquipmentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Equipment save(Equipment equipment) {
        entityManager.persist(equipment);
        return equipment;
    }

    public Optional<Equipment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Equipment.class, id));
    }

    public Optional<Equipment> findByName(String name) {
        TypedQuery<Equipment> query = entityManager.createQuery(
                "SELECT e FROM Equipment e WHERE e.name = :name",
                Equipment.class
        );

        query.setParameter("name", name);

        return query.getResultStream().findFirst();
    }

    public List<Equipment> listAll() {
        return entityManager
                .createQuery("SELECT e FROM Equipment e", Equipment.class)
                .getResultList();
    }

    @Transactional
    public Equipment update(Equipment equipment) {
        return entityManager.merge(equipment);
    }

    @Transactional
    public void delete(Equipment equipment) {
        entityManager.remove(
                entityManager.contains(equipment) ? equipment : entityManager.merge(equipment)
        );
    }
}

