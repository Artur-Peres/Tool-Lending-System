package br.edu.ifpb.tool_lending_system.service;

import br.edu.ifpb.tool_lending_system.DAO.CategoryDAO;
import br.edu.ifpb.tool_lending_system.DAO.EquipmentDAO;
import br.edu.ifpb.tool_lending_system.DTO.EquipmentRequestDTO;
import br.edu.ifpb.tool_lending_system.DTO.EquipmentResponseDTO;
import br.edu.ifpb.tool_lending_system.model.Category;
import br.edu.ifpb.tool_lending_system.model.Equipment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentDAO equipmentDAO;
    private final CategoryDAO categoryDAO;

    public EquipmentService(EquipmentDAO equipmentDAO, CategoryDAO categoryDAO) {
        this.equipmentDAO = equipmentDAO;
        this.categoryDAO = categoryDAO;
    }

    public EquipmentResponseDTO create(EquipmentRequestDTO dto) {

        equipmentDAO.findByName(dto.name())
                .ifPresent(equipment -> {
                    throw new RuntimeException(
                            "Equipamento já cadastrado! " + dto.name()
                    );
                });

        Category category = categoryDAO.findById(dto.categoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria não encontrada com o ID: "
                                        + dto.categoryId()
                        )
                );

        Equipment equipment = new Equipment();

        equipment.setName(dto.name());
        equipment.setDescription(dto.description());
        equipment.setMinimumTrustPoints(dto.minimumTrustPoints());
        equipment.setCategory(category);
        equipment.setAvailable(true);

        Equipment saved = equipmentDAO.save(equipment);

        return toResponseDTO(saved);
    }

    public EquipmentResponseDTO findById(Long id) {

        Equipment equipment = equipmentDAO.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipamento não encontrado com o ID: " + id
                        )
                );

        return toResponseDTO(equipment);
    }

    public EquipmentResponseDTO findByName(String name) {

        Equipment equipment = equipmentDAO.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipamento não encontrado com o nome: "
                                        + name
                        )
                );

        return toResponseDTO(equipment);
    }

    public List<EquipmentResponseDTO> listAll() {

        return equipmentDAO.listAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public EquipmentResponseDTO update(Long id, EquipmentRequestDTO dto) {

        Equipment equipment = equipmentDAO.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipamento não encontrado com o ID: " + id
                        )
                );

        equipmentDAO.findByName(dto.name())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new RuntimeException(
                            "Equipamento já cadastrado! " + dto.name()
                    );
                });

        Category category = categoryDAO.findById(dto.categoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria não encontrada com o ID: "
                                        + dto.categoryId()
                        )
                );

        equipment.setName(dto.name());
        equipment.setDescription(dto.description());
        equipment.setMinimumTrustPoints(dto.minimumTrustPoints());
        equipment.setCategory(category);

        Equipment updated = equipmentDAO.update(equipment);

        return toResponseDTO(updated);
    }

    public void delete(Long id) {

        Equipment equipment = equipmentDAO.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipamento não encontrado com o ID: " + id
                        )
                );

        equipmentDAO.delete(equipment);
    }

    private EquipmentResponseDTO toResponseDTO(Equipment equipment) {

        return new EquipmentResponseDTO(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getMinimumTrustPoints(),
                equipment.getAvailable(),
                equipment.getCategory().getId()
        );
    }
}

