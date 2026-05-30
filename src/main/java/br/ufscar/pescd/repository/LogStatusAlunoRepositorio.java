package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.LogStatusAluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogStatusAlunoRepositorio extends JpaRepository<LogStatusAluno, Long> {

    List<LogStatusAluno> findByAlunoOfertaOrderByAlteradoEmDesc(AlunoOferta alunoOferta);
}
