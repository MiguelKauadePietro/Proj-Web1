package br.ufscar.pescd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoDisciplina;

    private String nomeDisciplina;

    private String cursoDisciplina;

    @ManyToOne
    @JoinColumn(name = "professor_supervisor_id")
    private Usuario professorSupervisor;

    private String arquivoPath;

    private LocalDateTime enviadoEm;

    @Column(columnDefinition = "TEXT")
    private String parecer;

    private LocalDateTime aprovadoEm;

    @ManyToOne
    @JoinColumn(name = "aprovado_por_id")
    private Usuario aprovadoPor;
}
