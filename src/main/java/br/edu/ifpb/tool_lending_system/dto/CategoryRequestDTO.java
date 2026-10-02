package br.edu.ifpb.tool_lending_system.dto;

public class CategoryRequestDTO {

    private String name;
    private String description;

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}