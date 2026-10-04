package br.edu.ifpb.tool_lending_system.service;

import br.edu.ifpb.tool_lending_system.DAO.EquipmentDAO;
import br.edu.ifpb.tool_lending_system.DAO.LoanDAO;
import br.edu.ifpb.tool_lending_system.DAO.UserDAO;
import br.edu.ifpb.tool_lending_system.DTO.LoanRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.LoanResponseDTO;
import br.edu.ifpb.tool_lending_system.model.Equipment;
import br.edu.ifpb.tool_lending_system.model.Loan;
import br.edu.ifpb.tool_lending_system.model.StatusLoan;
import br.edu.ifpb.tool_lending_system.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanDAO loanDAO;
    private final UserDAO userDAO;
    private final EquipmentDAO equipmentDAO;

    @Value("${loan.late-penalty-points}")
    private int latePenaltyPoints;

    @Value("${loan.on-time-reward-points}")
    private int onTimeRewardPoints;

    public LoanService(LoanDAO loanDAO, UserDAO userDAO, EquipmentDAO equipmentDAO) {
        this.loanDAO = loanDAO;
        this.userDAO = userDAO;
        this.equipmentDAO = equipmentDAO;
    }

    @Transactional
    public LoanResponseDTO create(LoanRequestDTO dto) {

        User user = userDAO.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException(
                        "Usuário não encontrado com o ID: " + dto.userId()
                ));

        Equipment equipment = equipmentDAO.findById(dto.equipmentId())
                .orElseThrow(() -> new RuntimeException(
                        "Equipamento não encontrado com o ID: " + dto.equipmentId()
                ));

        validateLoan(user, equipment, dto.loanDays());

        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(dto.loanDays());

        Loan loan = new Loan();

        loan.setUser(user);
        loan.setEquipment(equipment);
        loan.setLoanDate(loanDate);
        loan.setDueDate(dueDate);
        loan.setStatus(StatusLoan.ACTIVE);

        equipment.setAvailable(false);
        equipmentDAO.update(equipment);

        Loan saved = loanDAO.save(loan);

        return toResponseDTO(saved);
    }

    @Transactional
    public LoanResponseDTO findById(Long id) {

        Loan loan = loanDAO.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Empréstimo não encontrado com o ID: " + id
                ));

        updateStatusIfLate(loan);

        return toResponseDTO(loan);
    }

    @Transactional
    public List<LoanResponseDTO> listAll() {

        return loanDAO.listAll()
                .stream()
                .map(loan -> {
                    updateStatusIfLate(loan);
                    return toResponseDTO(loan);
                })
                .toList();
    }

    @Transactional
    public LoanResponseDTO returnLoan(Long id) {

        Loan loan = loanDAO.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Empréstimo não encontrado com o ID: " + id
                ));

        if (loan.getStatus() == StatusLoan.RETURNED) {
            throw new RuntimeException(
                    "Este empréstimo já foi devolvido."
            );
        }

        LocalDate today = LocalDate.now();

        boolean late = today.isAfter(loan.getDueDate());

        if (late) {
            applyLatePenalty(loan.getUser());
        } else {
            applyOnTimeReward(loan.getUser());
        }

        loan.setReturnDate(today);
        loan.setStatus(StatusLoan.RETURNED);

        Equipment equipment = loan.getEquipment();
        equipment.setAvailable(true);

        equipmentDAO.update(equipment);

        Loan updated = loanDAO.update(loan);

        return toResponseDTO(updated);
    }

    private void validateLoan(User user, Equipment equipment, Integer loanDays) {

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new RuntimeException(
                    "Usuário está inativo."
            );
        }

        if (loanDays == null || loanDays <= 0) {
            throw new RuntimeException(
                    "A quantidade de dias do empréstimo deve ser maior que zero."
            );
        }

        updateLateLoans(user);

        long activeLoans = loanDAO.findByUserIdAndStatus(user.getId(), StatusLoan.ACTIVE).size();

        long lateLoans = loanDAO.findByUserIdAndStatus(user.getId(), StatusLoan.LATE).size();

        if (lateLoans > 0) {
            throw new RuntimeException(
                    "Usuário possui empréstimo atrasado."
            );
        }

        if (activeLoans >= 3) {
            throw new RuntimeException(
                    "Usuário já possui o limite de 3 empréstimos ativos."
            );
        }

        if (!Boolean.TRUE.equals(equipment.getAvailable())) {
            throw new RuntimeException(
                    "Equipamento não está disponível."
            );
        }

        if (user.getTrustPoints() < equipment.getMinimumTrustPoints()) {
            throw new RuntimeException(
                    "Usuário não possui pontos de confiança suficientes."
            );
        }
    }

    private void updateLateLoans(User user) {

        List<Loan> activeLoans = loanDAO.findByUserIdAndStatus(user.getId(), StatusLoan.ACTIVE);

        LocalDate today = LocalDate.now();

        for (Loan loan : activeLoans) {

            if (today.isAfter(loan.getDueDate())) {
                loan.setStatus(StatusLoan.LATE);
                loanDAO.update(loan);
            }
        }
    }

    private void updateStatusIfLate(Loan loan) {

        if (loan.getStatus() == StatusLoan.ACTIVE && LocalDate.now().isAfter(loan.getDueDate())) {
            loan.setStatus(StatusLoan.LATE);
            loanDAO.update(loan);
        }
    }

    private void applyLatePenalty(User user) {

        int newTrustPoints = Math.max(0, user.getTrustPoints() - latePenaltyPoints);

        user.setTrustPoints(newTrustPoints);

        userDAO.update(user);
    }

    private void applyOnTimeReward(User user) {

        int newTrustPoints = Math.min(100, user.getTrustPoints() + onTimeRewardPoints);

        user.setTrustPoints(newTrustPoints);

        userDAO.update(user);
    }

    private LoanResponseDTO toResponseDTO(Loan loan) {

        return new LoanResponseDTO(
                loan.getId(),
                loan.getUser().getId(),
                loan.getEquipment().getId(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getStatus()
        );
    }
}