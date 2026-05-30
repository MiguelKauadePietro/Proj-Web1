package br.ufscar.pescd.config;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.entity.enums.StatusAluno;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDataSeeder implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;
    private final OfertaRepositorio ofertaRepositorio;
    private final AlunoOfertaRepositorio alunoOfertaRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepositorio.count() > 0) {
            return;
        }

        log.info("Populando banco de dados com dados de exemplo...");

        criarUsuario("Admin Sistema", "admin@pescd.ufscar.br", "admin", "admin123", Perfil.ADMINISTRADOR);
        Usuario secretario = criarUsuario("Secretária Pós", "secretaria@pescd.ufscar.br", "secretaria", "secretario123", Perfil.SECRETARIO);
        Usuario prof1 = criarUsuario("Prof. Dr. João Silva", "joao.silva@ufscar.br", "joaosilva", "professor123", Perfil.PROFESSOR);
        criarUsuario("Profa. Dra. Maria Souza", "maria.souza@ufscar.br", "mariasouza", "professor123", Perfil.PROFESSOR);
        Usuario aluno1 = criarUsuario("Carlos Pereira", "carlos.pereira@estudante.ufscar.br", "carlospereira", "aluno123", Perfil.ALUNO);
        Usuario aluno2 = criarUsuario("Ana Lima", "ana.lima@estudante.ufscar.br", "analima", "aluno123", Perfil.ALUNO);

        Oferta oferta = ofertaRepositorio.save(new Oferta(
                null,
                "Estágio Docente — Computação 2025/1",
                "2025/1",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 7, 31),
                prof1,
                StatusOferta.EM_ANDAMENTO,
                secretario,
                LocalDateTime.now(),
                null, null, null, null
        ));

        alunoOfertaRepositorio.save(new AlunoOferta(null, aluno1, oferta, StatusAluno.NAO_ENVIADO, null, null));
        alunoOfertaRepositorio.save(new AlunoOferta(null, aluno2, oferta, StatusAluno.NAO_ENVIADO, null, null));

        log.info("Seed concluído: {} usuários, {} oferta(s)", usuarioRepositorio.count(), ofertaRepositorio.count());
    }

    private Usuario criarUsuario(String nome, String email, String username, String senha, Perfil perfil) {
        if (usuarioRepositorio.existsByUsername(username)) {
            return usuarioRepositorio.findByUsername(username).orElseThrow();
        }
        return usuarioRepositorio.save(new Usuario(null, nome, email, username, passwordEncoder.encode(senha), perfil, true));
    }
}
