package br.edu.ifpb.tool_lending_system.DAO;

import br.edu.ifpb.tool_lending_system.model.Loan;
import br.edu.ifpb.tool_lending_system.model.StatusLoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoanDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Loan save(Loan loan) {
        entityManager.persist(loan);
        return loan;
    }

    public Optional<Loan> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Loan.class, id));
    }

    public List<Loan> listAll() {
        return entityManager
                .createQuery("SELECT l FROM Loan l", Loan.class)
                .getResultList();
    }

    public List<Loan> findByUserId(Long userId) {
        TypedQuery<Loan> query = entityManager.createQuery(
                "SELECT l FROM Loan l WHERE l.user.id = :userId",
                Loan.class
        );

        query.setParameter("userId", userId);

        return query.getResultList();
    }

    public List<Loan> findByEquipmentId(Long equipmentId) {
        TypedQuery<Loan> query = entityManager.createQuery(
                "SELECT l FROM Loan l WHERE l.equipment.id = :equipmentId",
                Loan.class
        );

        query.setParameter("equipmentId", equipmentId);

        return query.getResultList();
    }

    public List<Loan> findByUserIdAndStatus(Long userId, StatusLoan status) {
        TypedQuery<Loan> query = entityManager.createQuery(
                """
                SELECT l FROM Loan l
                WHERE l.user.id = :userId
                AND l.status = :status
                """,
                Loan.class
        );

        query.setParameter("userId", userId);
        query.setParameter("status", status);

        return query.getResultList();
    }

    public List<Loan> findByEquipmentIdAndStatus(Long equipmentId, StatusLoan status) {
        TypedQuery<Loan> query = entityManager.createQuery(
                """
                SELECT l FROM Loan l
                WHERE l.equipment.id = :equipmentId
                AND l.status = :status
                """,
                Loan.class
        );

        query.setParameter("equipmentId", equipmentId);
        query.setParameter("status", status);

        return query.getResultList();
    }

    public List<Loan> findByStatus(StatusLoan status) {
        TypedQuery<Loan> query = entityManager.createQuery(
                "SELECT l FROM Loan l WHERE l.status = :status",
                Loan.class
        );

        query.setParameter("status", status);

        return query.getResultList();
    }

    @Transactional
    public Loan update(Loan loan) {
        return entityManager.merge(loan);
    }

    @Transactional
    public void delete(Loan loan) {
        entityManager.remove(
                entityManager.contains(loan) ? loan : entityManager.merge(loan)
        );
    }
}

