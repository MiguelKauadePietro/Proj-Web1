package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoOfertaRepositorio extends JpaRepository<AlunoOferta, Long> {

    long countByOferta(Oferta oferta);
}
