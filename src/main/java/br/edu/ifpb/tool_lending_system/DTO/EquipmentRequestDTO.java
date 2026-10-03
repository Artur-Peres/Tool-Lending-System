package br.edu.ifpb.tool_lending_system.DTO;

public record EquipmentRequestDTO(
        String name,
        String description,
        Integer minimumTrustPoints,
        Long categoryId
) {
}

