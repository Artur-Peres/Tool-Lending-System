package br.edu.ifpb.tool_lending_system.DTO;

public record UserResponseDTO(Long Id,String name,String cpf,String telefone,String email,Integer trustPoints,Boolean active) {
}
