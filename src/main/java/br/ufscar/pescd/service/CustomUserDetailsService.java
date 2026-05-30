package br.ufscar.pescd.service;

import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    public CustomUserDetailsService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByUsernameAndAtivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return new User(
                usuario.getUsername(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority(usuario.getPerfil().getRole()))
        );
    }
}
