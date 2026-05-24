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
public class LogStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private Oferta oferta;

    @Enumerated(EnumType.STRING)
    private StatusAluno statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusAluno statusNovo;

    @ManyToOne
    private Usuario alteradoPor;

    private LocalDateTime timestamp;

}
