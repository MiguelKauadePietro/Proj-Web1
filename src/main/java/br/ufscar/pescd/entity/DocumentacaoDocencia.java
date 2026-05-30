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
