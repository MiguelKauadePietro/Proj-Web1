package br.ufscar.pescd.config;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.PlanoTrabalho;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.entity.enums.StatusAluno;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import br.ufscar.pescd.repository.PlanoTrabalhoRepositorio;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDataSeeder implements CommandLineRunner {

    private static final String OFERTA_PRINCIPAL_NOME = "Estágio Docente – Computação 2025/1";
    private static final String OFERTA_PRINCIPAL_SEMESTRE = "2025/1";
    private static final String OFERTA_CONCLUIDA_NOME = "Estágio Docente – Computação 2024/2";
    private static final String OFERTA_AGUARDANDO_NOME = "Estágio Docente – Computação 2024/1";
    private static final String OFERTA_ATRASADA_NOME = "Estágio Docente – Computação 2023/2";

    private final UsuarioRepositorio usuarioRepositorio;
    private final OfertaRepositorio ofertaRepositorio;
    private final AlunoOfertaRepositorio alunoOfertaRepositorio;
    private final PlanoTrabalhoRepositorio planoTrabalhoRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Garantindo massa mínima de dados para o sistema...");

        Usuario admin = criarOuAtualizarUsuario(
                "Admin Sistema",
                "admin@pescd.ufscar.br",
                "admin",
                "admin123",
                Perfil.ADMINISTRADOR
        );
        Usuario secretario = criarOuAtualizarUsuario(
                "Secretária Pós",
                "secretaria@pescd.ufscar.br",
                "secretaria",
                "secretaria123",
                Perfil.SECRETARIO
        );
        Usuario professorSupervisor = criarOuAtualizarUsuario(
                "Prof. Dr. João Silva",
                "joao.silva@ufscar.br",
                "joaosilva",
                "professor123",
                Perfil.PROFESSOR
        );
        Usuario professorResponsavel = criarOuAtualizarUsuario(
                "Profa. Dra. Maria Prof",
                "mariaprof@ufscar.br",
                "mariaprof",
                "professor123",
                Perfil.PROFESSOR
        );
        Usuario alunoPlano = criarOuAtualizarUsuario(
                "Carlos Pereira",
                "carlos.pereira@estudante.ufscar.br",
                "carlospereira",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoDocumentacao = criarOuAtualizarUsuario(
                "Maria Aluna",
                "maria.aluna@estudante.ufscar.br",
                "mariaaluna",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoRelatorio = criarOuAtualizarUsuario(
                "Pedro Relatorio",
                "pedro.relatorio@estudante.ufscar.br",
                "pedrorelatorio",
                "aluno123",
                Perfil.ALUNO
        );

        Oferta ofertaPrincipal = criarOuAtualizarOferta(
                OFERTA_PRINCIPAL_NOME,
                OFERTA_PRINCIPAL_SEMESTRE,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2026, 12, 31),
                professorResponsavel,
                StatusOferta.EM_ANDAMENTO,
                secretario,
                null,
                null,
                null,
                null
        );

        garantirAlunoNaOferta(alunoPlano, ofertaPrincipal, StatusAluno.NAO_ENVIADO, null);
        garantirAlunoNaOferta(alunoDocumentacao, ofertaPrincipal, StatusAluno.NAO_ENVIADO, null);
        garantirAlunoNaOferta(alunoRelatorio, ofertaPrincipal, StatusAluno.PLANO_ENVIADO, professorSupervisor);

        criarOuAtualizarOferta(
                OFERTA_CONCLUIDA_NOME,
                "2024/2",
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 7, 31),
                professorResponsavel,
                StatusOferta.CONCLUIDA,
                secretario,
                secretario,
                LocalDateTime.now().minusDays(30),
                "Oferta concluída para teste.",
                "Somente leitura."
        );
        criarOuAtualizarOferta(
                OFERTA_AGUARDANDO_NOME,
                "2024/1",
                LocalDate.of(2024, 2, 15),
                LocalDate.of(2024, 6, 30),
                professorResponsavel,
                StatusOferta.AGUARDANDO_ENCERRAMENTO,
                secretario,
                null,
                null,
                "Encerramento solicitado.",
                "Aguardando homologação."
        );
        criarOuAtualizarOferta(
                OFERTA_ATRASADA_NOME,
                "2023/2",
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2023, 12, 15),
                professorResponsavel,
                StatusOferta.EM_ATRASO,
                secretario,
                null,
                null,
                null,
                null
        );

        log.info("Seed concluído: {} usuários, {} oferta(s)", usuarioRepositorio.count(), ofertaRepositorio.count());
    }

    private Usuario criarOuAtualizarUsuario(String nome, String email, String username, String senha, Perfil perfil) {
        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseGet(Usuario::new);

        usuario.setNomeCompleto(nome);
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(perfil);
        usuario.setAtivo(true);

        return usuarioRepositorio.save(usuario);
    }

    private Oferta criarOuAtualizarOferta(
            String nome,
            String semestre,
            LocalDate dataInicio,
            LocalDate dataFim,
            Usuario professorResponsavel,
            StatusOferta status,
            Usuario criadoPor,
            Usuario encerradoPor,
            LocalDateTime encerradoEm,
            String licoesAprendidas,
            String instrucaoEncerramento) {
        Oferta oferta = buscarOfertaPorNomeSemestreOuSemestre(nome, semestre)
                .orElseGet(Oferta::new);

        oferta.setNome(nome);
        oferta.setSemestre(semestre);
        oferta.setDataInicio(dataInicio);
        oferta.setDataFim(dataFim);
        oferta.setProfessorResponsavel(professorResponsavel);
        oferta.setStatus(status);
        oferta.setCriadoPor(criadoPor);
        if (oferta.getCriadoEm() == null) {
            oferta.setCriadoEm(LocalDateTime.now());
        }
        oferta.setEncerradoPor(encerradoPor);
        oferta.setEncerradoEm(encerradoEm);
        oferta.setLicoesAprendidas(licoesAprendidas);
        oferta.setInstrucaoEncerramento(instrucaoEncerramento);

        return ofertaRepositorio.save(oferta);
    }

    private Optional<Oferta> buscarOfertaPorNomeSemestreOuSemestre(String nome, String semestre) {
        Optional<Oferta> ofertaPorNome = ofertaRepositorio.findByNomeAndSemestre(nome, semestre);
        if (ofertaPorNome.isPresent()) {
            return ofertaPorNome;
        }

        return ofertaRepositorio.findAllByOrderBySemestreDesc().stream()
                .filter(oferta -> semestre.equals(oferta.getSemestre()) && nomeParecido(nome, oferta.getNome()))
                .findFirst();
    }

    private boolean nomeParecido(String esperado, String atual) {
        return normalizarNome(esperado).equals(normalizarNome(atual));
    }

    private String normalizarNome(String nome) {
        return nome == null ? "" : nome.replace('—', '-').replace('–', '-').trim();
    }

    private void garantirAlunoNaOferta(
            Usuario aluno,
            Oferta oferta,
            StatusAluno status,
            Usuario professorSupervisor) {
        AlunoOferta alunoOferta = alunoOfertaRepositorio.findByOferta(oferta).stream()
                .filter(vinculo -> vinculo.getAluno().getId().equals(aluno.getId()))
                .findFirst()
                .orElseGet(AlunoOferta::new);

        alunoOferta.setAluno(aluno);
        alunoOferta.setOferta(oferta);
        alunoOferta.setStatus(status);

        if (status == StatusAluno.PLANO_APROVADO) {
            alunoOferta.setPlano(garantirPlanoMinimo(alunoOferta.getPlano(), professorSupervisor));
        }

        alunoOfertaRepositorio.save(alunoOferta);
    }

    private PlanoTrabalho garantirPlanoMinimo(PlanoTrabalho planoExistente, Usuario professorSupervisor) {
        PlanoTrabalho plano = planoExistente != null
                ? planoExistente
                : new PlanoTrabalho();

        plano.setCodigoDisciplina("DC-001");
        plano.setNomeDisciplina("Estágio Supervisionado em Docência");
        plano.setCursoDisciplina("Computação");
        plano.setProfessorSupervisor(professorSupervisor);
        plano.setArquivoPath("uploads/planos/plano-seed-pedrorelatorio.pdf");
        if (plano.getEnviadoEm() == null) {
            plano.setEnviadoEm(LocalDateTime.now().minusDays(10));
        }
        plano.setParecer("Plano aprovado para testes.");
        if (plano.getAprovadoEm() == null) {
            plano.setAprovadoEm(LocalDateTime.now().minusDays(5));
        }
        plano.setAprovadoPor(professorSupervisor);

        return planoTrabalhoRepositorio.save(plano);
    }
}
