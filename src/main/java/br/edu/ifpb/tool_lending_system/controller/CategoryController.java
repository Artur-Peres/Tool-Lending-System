package br.edu.ifpb.tool_lending_system.controller;

import br.edu.ifpb.tool_lending_system.dto.CategoryRequestDTO;
import br.edu.ifpb.tool_lending_system.dto.CategoryResponseDTO;
import br.edu.ifpb.tool_lending_system.exception.CategoryNotFoundException;
import br.edu.ifpb.tool_lending_system.model.Category;
import br.edu.ifpb.tool_lending_system.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService();
    }

    @PostMapping
    public CategoryResponseDTO cadastrar(
            @RequestBody CategoryRequestDTO request) {

        Category category = new Category(
                request.getName(),
                request.getDescription()
        );

        categoryService.cadastrar(category);

        return converterParaResponse(category);
    }

    @GetMapping
    public List<CategoryResponseDTO> listarTodos() {

        return categoryService.listarTodos()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO buscarPorId(@PathVariable Long id) {

        Category category = categoryService.buscarPorId(id);

        return converterParaResponse(category);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO request) {

        Category existente = categoryService.buscarPorId(id);

        existente.setName(request.getName());
        existente.setDescription(request.getDescription());

        Category atualizada = categoryService.atualizar(existente);

        return converterParaResponse(atualizada);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        categoryService.excluir(id);
    }

    private CategoryResponseDTO converterParaResponse(Category category) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> tratarCategoriaNaoEncontrada(
            CategoryNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarArgumentoInvalido(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}