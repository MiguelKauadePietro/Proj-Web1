package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.DocumentacaoDocencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentacaoDocenciaRepositorio extends JpaRepository<DocumentacaoDocencia, Long> {
}