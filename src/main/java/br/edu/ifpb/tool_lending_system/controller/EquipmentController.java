package br.edu.ifpb.tool_lending_system.controller;

import br.edu.ifpb.tool_lending_system.DTO.EquipmentRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.EquipmentResponseDTO;
import br.edu.ifpb.tool_lending_system.service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<EquipmentResponseDTO> create(
            @RequestBody EquipmentRequestDTO dto
    ) {
        EquipmentResponseDTO response = equipmentService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> listAll() {
        return ResponseEntity.ok(
                equipmentService.listAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                equipmentService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> update(
            @PathVariable Long id,
            @RequestBody EquipmentRequestDTO dto
    ) {
        return ResponseEntity.ok(
                equipmentService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        equipmentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
