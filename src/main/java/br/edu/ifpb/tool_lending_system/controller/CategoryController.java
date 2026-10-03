package br.edu.ifpb.tool_lending_system.controller;

import br.edu.ifpb.tool_lending_system.DTO.CategoryRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.CategoryResponseDTO;
import br.edu.ifpb.tool_lending_system.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @RequestBody CategoryRequestDTO dto
    ) {
        CategoryResponseDTO response = categoryService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listAll() {
        return ResponseEntity.ok(
                categoryService.listAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                categoryService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto
    ) {
        return ResponseEntity.ok(
                categoryService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
