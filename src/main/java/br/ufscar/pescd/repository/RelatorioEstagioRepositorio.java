package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.entity.enums.StatusAluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatorioEstagioRepositorio extends JpaRepository<RelatorioEstagio, Long> {

    List<RelatorioEstagio> findByParecer(StatusAluno parecer);

}
