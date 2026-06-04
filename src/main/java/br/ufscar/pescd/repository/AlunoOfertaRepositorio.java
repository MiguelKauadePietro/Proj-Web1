package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlunoOfertaRepositorio extends JpaRepository<AlunoOferta, Long> {

    long countByOferta(Oferta oferta);

    List<AlunoOferta> findByOferta(Oferta oferta);

    boolean existsByOfertaAndAluno(Oferta oferta, Usuario aluno);

    @Query("""
            select ao
            from AlunoOferta ao
            where ao.aluno = :aluno
            order by ao.oferta.semestre desc, ao.id desc
            """)
    List<AlunoOferta> findByAlunoOrdenado(Usuario aluno);

    Optional<AlunoOferta> findByIdAndAluno(Long id, Usuario aluno);
}
