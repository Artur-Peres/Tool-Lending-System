package br.edu.ifpb.tool_lending_system.DTO;

public record LoanRequestDTO(
        Long userId,
        Long equipmentId,
        Integer loanDays
) {
}
