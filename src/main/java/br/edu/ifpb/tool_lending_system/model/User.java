package br.edu.ifpb.tool_lending_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(unique = true,nullable = false,length = 14)
    private String cpf;

    private String email;
    private String telefone;

    @Column(name="Trust Points", nullable = false)
    private Integer trustPoints = 100;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "User")
    private List<Loan> loans=new ArrayList<Loan>();

}
