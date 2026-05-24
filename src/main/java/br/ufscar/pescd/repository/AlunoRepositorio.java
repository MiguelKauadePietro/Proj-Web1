package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.Aluno;
import br.ufscar.pescd.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepositorio extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByEmail(String email);

    List<Aluno> findByOfertas(Oferta oferta);

}
