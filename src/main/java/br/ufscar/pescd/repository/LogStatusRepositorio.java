package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.Aluno;
import br.ufscar.pescd.entity.LogStatus;
import br.ufscar.pescd.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogStatusRepositorio extends JpaRepository<LogStatus, Long> {

    List<LogStatus> findByAlunoAndOfertaOrderByTimestampAsc(Aluno aluno, Oferta oferta);

}
