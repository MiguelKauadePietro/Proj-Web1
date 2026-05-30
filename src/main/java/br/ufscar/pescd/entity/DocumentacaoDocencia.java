package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.Nota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentacao_docencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentacaoDocencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeInstituicao;

    private String nomeDisciplina;

    private String cursoDisciplina;

    private Integer cargaHoraria;

    private String arquivoPath;

    private LocalDateTime enviadaEm;

    @Column(columnDefinition = "TEXT")
    private String parecer;

    private Double frequencia;

    @Enumerated(EnumType.STRING)
    private Nota nota;

    private LocalDateTime analisadaEm;

    @ManyToOne
    @JoinColumn(name = "analisada_por_id")
    private Usuario analisadaPor;
}
