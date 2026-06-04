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

    private static final String OFERTA_NOME_TESTE = "Estágio Docente — Computação 2025/1";
    private static final String OFERTA_SEMESTRE_TESTE = "2025/1";

    private final UsuarioRepositorio usuarioRepositorio;
    private final OfertaRepositorio ofertaRepositorio;
    private final AlunoOfertaRepositorio alunoOfertaRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Garantindo massa mínima de dados para o sistema...");

        criarUsuario("Admin Sistema", "admin@pescd.ufscar.br", "admin", "admin123", Perfil.ADMINISTRADOR);
        Usuario secretario = criarUsuario("Secretária Pós", "secretaria@pescd.ufscar.br", "secretaria", "secretario123", Perfil.SECRETARIO);
        Usuario prof1 = criarUsuario("Prof. Dr. João Silva", "joao.silva@ufscar.br", "joaosilva", "professor123", Perfil.PROFESSOR);
        criarUsuario("Profa. Dra. Maria Souza", "maria.souza@ufscar.br", "mariasouza", "professor123", Perfil.PROFESSOR);
        Usuario aluno1 = criarUsuario("Carlos Pereira", "carlos.pereira@estudante.ufscar.br", "carlospereira", "aluno123", Perfil.ALUNO);
        Usuario aluno2 = criarUsuario("Ana Lima", "ana.lima@estudante.ufscar.br", "analima", "aluno123", Perfil.ALUNO);

        Oferta oferta = criarOfertaTeste(secretario, prof1);

        vincularAlunoNaOfertaSeNecessario(aluno1, oferta);
        vincularAlunoNaOfertaSeNecessario(aluno2, oferta);

        log.info("Seed concluído: {} usuários, {} oferta(s)", usuarioRepositorio.count(), ofertaRepositorio.count());
    }

    private Usuario criarUsuario(String nome, String email, String username, String senha, Perfil perfil) {
        if (usuarioRepositorio.existsByUsername(username)) {
            return usuarioRepositorio.findByUsername(username).orElseThrow();
        }
        return usuarioRepositorio.save(new Usuario(null, nome, email, username, passwordEncoder.encode(senha), perfil, true));
    }

    private Oferta criarOfertaTeste(Usuario secretario, Usuario professorResponsavel) {
        return ofertaRepositorio.findByNomeAndSemestre(OFERTA_NOME_TESTE, OFERTA_SEMESTRE_TESTE)
                .orElseGet(() -> ofertaRepositorio.save(new Oferta(
                        null,
                        OFERTA_NOME_TESTE,
                        OFERTA_SEMESTRE_TESTE,
                        LocalDate.of(2025, 3, 1),
                        LocalDate.of(2025, 7, 31),
                        professorResponsavel,
                        StatusOferta.EM_ANDAMENTO,
                        secretario,
                        LocalDateTime.now(),
                        null, null, null, null
                )));
    }

    private void vincularAlunoNaOfertaSeNecessario(Usuario aluno, Oferta oferta) {
        if (alunoOfertaRepositorio.existsByOfertaAndAluno(oferta, aluno)) {
            return;
        }

        alunoOfertaRepositorio.save(new AlunoOferta(null, aluno, oferta, StatusAluno.NAO_ENVIADO, null, null));
    }
}
