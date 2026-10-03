package br.edu.ifpb.tool_lending_system.DTO;

import br.edu.ifpb.tool_lending_system.model.StatusLoan;

import java.time.LocalDate;

public record LoanResponseDTO(
        Long id,
        Long userId,
        Long equipmentId,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        StatusLoan status
) {
}

