package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlunoOfertaRepositorio extends JpaRepository<AlunoOferta, Long> {

    long countByOferta(Oferta oferta);
    List<AlunoOferta> findByOferta(Oferta oferta);
    boolean existsByOfertaAndAluno(Oferta oferta, Usuario aluno);
}
