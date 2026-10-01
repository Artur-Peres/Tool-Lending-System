package br.edu.ifpb.tool_lending_system.service;

import br.edu.ifpb.tool_lending_system.dao.CategoryDAO;
import br.edu.ifpb.tool_lending_system.exception.CategoryNotFoundException;
import br.edu.ifpb.tool_lending_system.model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public void cadastrar(Category category) {

        if (category == null) {
            throw new IllegalArgumentException(
                    "A categoria não pode ser nula."
            );
        }

        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "O nome da categoria é obrigatório."
            );
        }

        if (categoryDAO.existePorNome(category.getName())) {
            throw new IllegalArgumentException(
                    "Já existe uma categoria com esse nome."
            );
        }

        categoryDAO.salvar(category);
    }

    public Category buscarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID da categoria inválido."
            );
        }

        Category category = categoryDAO.buscarPorId(id);

        if (category == null) {
            throw new CategoryNotFoundException(
                    "Categoria com ID " + id + " não encontrada."
            );
        }

        return category;
    }

    public List<Category> listarTodos() {
        return categoryDAO.listarTodos();
    }

    public Category atualizar(Category category) {

        if (category == null) {
            throw new IllegalArgumentException(
                    "A categoria não pode ser nula."
            );
        }

        if (category.getId() == null || category.getId() <= 0) {
            throw new IllegalArgumentException(
                    "ID da categoria inválido."
            );
        }

        buscarPorId(category.getId());

        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "O nome da categoria é obrigatório."
            );
        }

        if (categoryDAO.existePorNomeEIdDiferente(
                category.getName(),
                category.getId())) {

            throw new IllegalArgumentException(
                    "Já existe outra categoria com esse nome."
            );
        }

        return categoryDAO.atualizar(category);
    }

    public void excluir(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID da categoria inválido."
            );
        }

        buscarPorId(id);

        categoryDAO.excluir(id);
    }
}