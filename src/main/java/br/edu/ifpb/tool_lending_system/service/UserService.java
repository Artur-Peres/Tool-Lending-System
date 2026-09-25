package br.edu.ifpb.tool_lending_system.service;


import br.edu.ifpb.tool_lending_system.DAO.UserDAO;
import br.edu.ifpb.tool_lending_system.DTO.UserRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.UserResponseDTO;
import br.edu.ifpb.tool_lending_system.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO=userDAO;
    }

    public UserResponseDTO create(UserRequestDTO dto){
        userDAO.findByCpf(dto.cpf()).ifPresent(u -> {throw new RuntimeException("CPF já cadastrado! "+ dto.cpf());});

        User user = new User();
        user.setName(dto.name());
        user.setCpf(dto.cpf());
        user.setTelefone(dto.telefone());
        user.setEmail(dto.email());

        User saved= userDAO.save(user);
        return toResponseDTO(user);
    }

    public UserResponseDTO findById(Long id){
        User user= userDAO.findById(id).orElseThrow(()-> new RuntimeException("Usuario Não Encontrado com o ID: " + id));

        return  toResponseDTO(user);
    }

    public UserResponseDTO findByCpf(String cpf){
        User user = userDAO.findByCpf(cpf).orElseThrow(()-> new RuntimeException("Cliente Não Encontrado com o CPF: " + cpf) );
        return  toResponseDTO(user);
    }

    public List<UserResponseDTO> listAll(){
        return userDAO.listAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
    public UserResponseDTO update(Long id, UserRequestDTO dto){
        User user= userDAO.findById(id).orElseThrow(()-> new RuntimeException("Usuario Não Encontrado com o ID: " + id));

        userDAO.findByCpf(dto.cpf())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {throw new RuntimeException("CPF já cadastrado! " + dto.cpf());

                });
        user.setName(dto.name());
        user.setCpf(dto.cpf());
        user.setTelefone(dto.telefone());
        user.setEmail(dto.email());

        User updated = userDAO.update(user);
        return toResponseDTO(updated);

    }

    public void delete(Long id){
        User user = userDAO.findById(id).orElseThrow(()-> new RuntimeException("Cliente Não encontrado com ID: " + id));
        userDAO.delete(user);
    }

    public void adjustTrustPoints(User user,int points){
        int newScore = user.getTrustPoints() + points;
        user.setTrustPoints(Math.max(0,Math.min(100,newScore)));
        userDAO.update(user);
    }

    public boolean hasAlateLoans(User user){
        return user.getLoans().stream()
                .anyMatch(loan -> loan.getStatus() == StatusLoan.Late);

    }

    public long countActiveLoans(User user){
        return user.getLoans().stream()
                .filter(loan -> loan.getStatus() == StatusLoan.Active).count();

    }
    public boolean canTakeOutLoans(User user){
        if (!Boolean.TRUE.equals(user.getActive())){
            return false;
        }
        if (hasAlateLoans(user)){
            return false;
        }
        return countActiveLoans(user) < 3;
    }

    private UserResponseDTO toResponseDTO(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getTelefone(),
                user.getEmail(),
                user.getTrustPoints(),
                user.getActive());
    }
}
