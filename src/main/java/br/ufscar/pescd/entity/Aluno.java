package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.StatusAluno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ra;

    @Column(unique = true)
    private String email;

    private String nome;

    @Enumerated(EnumType.STRING)
    private StatusAluno status;

    @ManyToMany(mappedBy = "alunos")
    private List<Oferta> ofertas;

}
