package br.ufscar.pescd.service;

import br.ufscar.pescd.dto.UsuarioFormDto;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.exception.AlterarProprioPerfilException;
import br.ufscar.pescd.exception.DesativarProprioUsuarioException;
import br.ufscar.pescd.exception.EmailJaCadastradoException;
import br.ufscar.pescd.exception.UsuarioNaoEncontradoException;
import br.ufscar.pescd.exception.UsernameJaCadastradoException;

import br.ufscar.pescd.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarGerenciaveis() {
        return usuarioRepositorio.findByPerfilIn(
                List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR)
        );
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    @Transactional
    public Usuario criar(UsuarioFormDto dto) {
        if (usuarioRepositorio.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException();
        }
        if (usuarioRepositorio.existsByUsername(dto.getUsername())) {
            throw new UsernameJaCadastradoException();
        }

        return usuarioRepositorio.save(dto.toUsuario(passwordEncoder));
    }

    @Transactional
    public Usuario editar(Long id, UsuarioFormDto dto, String usernameLogado) {
        Usuario usuario = buscarPorId(id);

        if (usuario.getUsername().equals(usernameLogado) && usuario.getPerfil() != dto.getPerfil()) {
            throw new AlterarProprioPerfilException();
        }

        if (usuarioRepositorio.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new EmailJaCadastradoException();
        }

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil());

        if (StringUtils.hasText(dto.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void desativar(Long id, String usernameLogado) {
        Usuario usuario = buscarPorId(id);
        if (usuario.getUsername().equals(usernameLogado)) {
            throw new DesativarProprioUsuarioException();
        }
        usuario.setAtivo(false);
        usuarioRepositorio.save(usuario);
    }
}
