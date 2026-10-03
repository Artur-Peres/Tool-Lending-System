package br.edu.ifpb.tool_lending_system.DTO;

public record EquipmentResponseDTO(
        Long id,
        String name,
        String description,
        Integer minimumTrustPoints,
        Boolean available,
        Long categoryId
) {
}

