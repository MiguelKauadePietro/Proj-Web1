package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.RelatorioEstagio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelatorioEstagioRepositorio extends JpaRepository<RelatorioEstagio, Long> {

    Optional<RelatorioEstagio> findByAlunoOferta(AlunoOferta alunoOferta);
}
