package br.ufscar.pescd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String semestre;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    @ManyToOne
    private Usuario professorResponsavel;

    @ManyToMany
    @JoinTable(
        name = "oferta_aluno",
        joinColumns = @JoinColumn(name = "oferta_id"),
        inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunos;

}