package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.StatusAluno;
import jakarta.persistence.*;
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
