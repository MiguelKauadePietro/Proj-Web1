package br.ufscar.pescd.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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

}