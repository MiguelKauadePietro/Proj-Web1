package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.StatusOferta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String semestre;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "professor_responsavel_id")
    private Usuario professorResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOferta status = StatusOferta.EM_ANDAMENTO;

    @ManyToOne
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "encerrado_por_id")
    private Usuario encerradoPor;

    private LocalDateTime encerradoEm;

    @Column(columnDefinition = "TEXT")
    private String licoesAprendidas;

    @Column(columnDefinition = "TEXT")
    private String instrucaoEncerramento;

}
