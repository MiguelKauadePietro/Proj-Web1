package br.ufscar.pescd.entity;

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
public class DocumentacaoDocencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private Oferta oferta;

    private Integer cargaHoraria;

    private String comprovantes;

    @Enumerated(EnumType.STRING)
    private StatusAluno status;

    private LocalDateTime dataEnvio;

}
