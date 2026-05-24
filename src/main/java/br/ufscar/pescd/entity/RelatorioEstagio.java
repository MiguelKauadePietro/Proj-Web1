package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.Nota;
import br.ufscar.pescd.entity.enums.StatusAluno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioEstagio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private Oferta oferta;

    private Double frequencia;

    private String arquivoPdf;

    @Enumerated(EnumType.STRING)
    private Nota nota;

    @Enumerated(EnumType.STRING)
    private StatusAluno parecer;

    private LocalDateTime dataEnvio;

    private LocalDateTime dataAvaliacao;

}
