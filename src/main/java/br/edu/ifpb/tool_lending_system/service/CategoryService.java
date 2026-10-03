package br.edu.ifpb.tool_lending_system.service;

import br.edu.ifpb.tool_lending_system.DAO.CategoryDAO;
import br.edu.ifpb.tool_lending_system.DTO.CategoryRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.CategoryResponseDTO;
import br.edu.ifpb.tool_lending_system.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {

        categoryDAO.findByName(dto.name())
                .ifPresent(category -> {
                    throw new RuntimeException(
                            "Categoria já cadastrada! " + dto.name()
                    );
                });

        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        Category saved = categoryDAO.save(category);

        return toResponseDTO(saved);
    }

    public CategoryResponseDTO findById(Long id) {

        Category category = categoryDAO.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria não encontrada com o ID: " + id
                        )
                );

        return toResponseDTO(category);
    }

    public CategoryResponseDTO findByName(String name) {

        Category category = categoryDAO.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria não encontrada com o nome: " + name
                        )
                );

        return toResponseDTO(category);
    }

    public List<CategoryResponseDTO> listAll() {

        return categoryDAO.listAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {

        Category category = categoryDAO.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria não encontrada com o ID: " + id
                        )
                );

        categoryDAO.findByName(dto.name())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new RuntimeException(
                            "Categoria já cadastrada! " + dto.name()
                    );
                });

        category.setName(dto.name());
        category.setDescription(dto.description());

        Category updated = categoryDAO.update(category);

        return toResponseDTO(updated);
    }

    public void delete(Long id) {

        Category category = categoryDAO.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria não encontrada com o ID: " + id
                        )
                );

        categoryDAO.delete(category);
    }

    private CategoryResponseDTO toResponseDTO(Category category) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}

