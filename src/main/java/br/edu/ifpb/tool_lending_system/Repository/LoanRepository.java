package br.edu.ifpb.tool_lending_system.Repository;

import br.edu.ifpb.tool_lending_system.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}