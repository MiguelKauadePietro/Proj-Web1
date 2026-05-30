package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfertaRepositorio extends JpaRepository<Oferta, Long> {

    List<Oferta> findAllByOrderBySemestreDesc();
}
