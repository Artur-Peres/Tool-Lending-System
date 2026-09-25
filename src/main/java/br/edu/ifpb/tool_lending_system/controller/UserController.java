package br.edu.ifpb.tool_lending_system.controller;


import br.edu.ifpb.tool_lending_system.DTO.UserRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.UserResponseDTO;
import br.edu.ifpb.tool_lending_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create (@RequestBody UserRequestDTO dto) {
        UserResponseDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
        return  ResponseEntity.ok(userService.findById(id));
    }
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UserResponseDTO> findByCpf(@PathVariable String cpf){
        return  ResponseEntity.ok(userService.findByCpf(cpf));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserResponseDTO>> listAll(){
        return  ResponseEntity.ok(userService.listAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        return   ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
