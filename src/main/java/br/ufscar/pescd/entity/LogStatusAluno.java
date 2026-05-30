package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.StatusAluno;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "log_status_aluno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogStatusAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_oferta_id")
    private AlunoOferta alunoOferta;

    @Enumerated(EnumType.STRING)
    private StatusAluno statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAluno statusNovo;

    private LocalDateTime alteradoEm;

    @ManyToOne
    @JoinColumn(name = "alterado_por_id")
    private Usuario alteradoPor;

    @Column(columnDefinition = "TEXT")
    private String observacao;
}
