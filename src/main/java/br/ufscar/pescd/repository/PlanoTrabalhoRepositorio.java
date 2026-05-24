package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.Aluno;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.PlanoTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoTrabalhoRepositorio extends JpaRepository<PlanoTrabalho, Long> {

    Optional<PlanoTrabalho> findByAlunoAndOferta(Aluno aluno, Oferta oferta);

}
