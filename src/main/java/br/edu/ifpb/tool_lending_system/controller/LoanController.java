package br.edu.ifpb.tool_lending_system.controller;

import br.edu.ifpb.tool_lending_system.DTO.LoanRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.LoanResponseDTO;
import br.edu.ifpb.tool_lending_system.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(
            @RequestBody LoanRequestDTO dto
    ) {
        LoanResponseDTO response = loanService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> listAll() {
        return ResponseEntity.ok(
                loanService.listAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                loanService.findById(id)
        );
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanResponseDTO> returnLoan(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                loanService.returnLoan(id)
        );
    }
}