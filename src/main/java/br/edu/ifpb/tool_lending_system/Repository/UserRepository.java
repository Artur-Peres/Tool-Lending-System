package br.edu.ifpb.tool_lending_system.Repository;

import br.edu.ifpb.tool_lending_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}