package br.edu.ifpb.tool_lending_system.controller;

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
    public Category cadastrar(@RequestBody Category category) {
        categoryService.cadastrar(category);
        return category;
    }

    @GetMapping
    public List<Category> listarTodos() {
        return categoryService.listarTodos();
    }

    @GetMapping("/{id}")
    public Category buscarPorId(@PathVariable Long id) {
        return categoryService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Category atualizar(
            @PathVariable Long id,
            @RequestBody Category category) {

        Category existente = categoryService.buscarPorId(id);

        existente.setName(category.getName());
        existente.setDescription(category.getDescription());

        return categoryService.atualizar(existente);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        categoryService.excluir(id);
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