package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.Nota;
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
@Table(name = "relatorio_estagio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioEstagio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_oferta_id")
    private AlunoOferta alunoOferta;

    private String arquivoPath;

    private Boolean indicadorFrequencia;

    private LocalDateTime enviadoEm;

    @Column(columnDefinition = "TEXT")
    private String parecerSupervisor;

    private Double frequenciaSupervisor;

    @Enumerated(EnumType.STRING)
    private Nota sugestaoNotaSupervisor;

    private LocalDateTime aprovadoPorSupervisorEm;

    @Column(columnDefinition = "TEXT")
    private String parecerResponsavel;

    private Double frequenciaResponsavel;

    @Enumerated(EnumType.STRING)
    private Nota notaResponsavel;

    private LocalDateTime aprovadoPorResponsavelEm;

    @ManyToOne
    @JoinColumn(name = "aprovado_por_responsavel_id")
    private Usuario aprovadoPorResponsavel;
}
