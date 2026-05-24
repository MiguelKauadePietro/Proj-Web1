package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.Aluno;
import br.ufscar.pescd.entity.DocumentacaoDocencia;
import br.ufscar.pescd.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentacaoDocenciaRepositorio extends JpaRepository<DocumentacaoDocencia, Long> {

    List<DocumentacaoDocencia> findByAlunoAndOferta(Aluno aluno, Oferta oferta);

}
