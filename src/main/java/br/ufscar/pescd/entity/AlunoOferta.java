package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.StatusAluno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aluno_oferta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoOferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id")
    private Usuario aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAluno status = StatusAluno.NAO_ENVIADO;

    @OneToOne
    @JoinColumn(name = "plano_id")
    private PlanoTrabalho plano;

    @OneToOne
    @JoinColumn(name = "documentacao_id")
    private DocumentacaoDocencia documentacao;
}
