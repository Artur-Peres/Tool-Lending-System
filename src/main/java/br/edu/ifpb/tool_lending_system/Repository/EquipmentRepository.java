package br.edu.ifpb.tool_lending_system.Repository;

import br.edu.ifpb.tool_lending_system.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}