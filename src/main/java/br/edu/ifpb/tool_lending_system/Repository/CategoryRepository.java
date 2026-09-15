package br.edu.ifpb.tool_lending_system.Repository;

import br.edu.ifpb.tool_lending_system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
