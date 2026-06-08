package br.ufscar.pescd.repository;

import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsernameAndAtivoTrue(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsername(String username);

    List<Usuario> findByPerfilIn(List<Perfil> perfis);

    List<Usuario> findByPerfilInAndAtivoTrue(List<Perfil> perfis);
}
