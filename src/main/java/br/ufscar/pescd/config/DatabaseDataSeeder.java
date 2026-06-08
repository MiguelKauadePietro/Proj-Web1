package br.ufscar.pescd.config;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.DocumentacaoDocencia;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.PlanoTrabalho;
import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Nota;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.entity.enums.StatusAluno;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.DocumentacaoDocenciaRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import br.ufscar.pescd.repository.PlanoTrabalhoRepositorio;
import br.ufscar.pescd.repository.RelatorioEstagioRepositorio;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDataSeeder implements CommandLineRunner {

    private static final String OFERTA_PRINCIPAL_NOME = "Algoritmos e Programação 1";
    private static final String OFERTA_PRINCIPAL_SEMESTRE = "2025/1";
    private static final String OFERTA_CONCLUIDA_NOME = "Estruturas de Dados";
    private static final String OFERTA_AGUARDANDO_NOME = "Banco de Dados";
    private static final String OFERTA_ATRASADA_NOME = "Engenharia de Software";
    private static final String PDF_PLANO_PEDRO =
            "uploads/planos/20260606185753-40ef7be9-18c3-4309-abc3-1e259d6977e4-WEB1_-_Estruturacao_AA1.pdf";

    private final UsuarioRepositorio usuarioRepositorio;
    private final OfertaRepositorio ofertaRepositorio;
    private final AlunoOfertaRepositorio alunoOfertaRepositorio;
    private final PlanoTrabalhoRepositorio planoTrabalhoRepositorio;
    private final DocumentacaoDocenciaRepositorio documentacaoDocenciaRepositorio;
    private final RelatorioEstagioRepositorio relatorioEstagioRepositorio;
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
        Usuario professorResponsavel2 = criarOuAtualizarUsuario(
                "Prof. Dr. Roberto Alves",
                "roberto.alves@ufscar.br",
                "robertoalves",
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
        Usuario alunoPlanoAprovado = criarOuAtualizarUsuario(
                "Ana Plano Aprovado",
                "ana.plano@estudante.ufscar.br",
                "anaplano",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoDocEnviada = criarOuAtualizarUsuario(
                "Diego Documentacao",
                "diego.doc@estudante.ufscar.br",
                "diegodoc",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoRelatorioEnviado = criarOuAtualizarUsuario(
                "Bruno Relatorio Enviado",
                "bruno.relatorio@estudante.ufscar.br",
                "brunorelatorio",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoRelatorioAprovado = criarOuAtualizarUsuario(
                "Clara Relatorio Aprovado",
                "clara.relatorio@estudante.ufscar.br",
                "clararelatorio",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoConcluido1 = criarOuAtualizarUsuario(
                "Lucas Concluido",
                "lucas.concluido@estudante.ufscar.br",
                "lucasconcluido",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoConcluido2 = criarOuAtualizarUsuario(
                "Beatriz Concluida",
                "beatriz.concluida@estudante.ufscar.br",
                "beatrizconcluida",
                "aluno123",
                Perfil.ALUNO
        );
        Usuario alunoAguardando = criarOuAtualizarUsuario(
                "Rafael Aguardando",
                "rafael.aguardando@estudante.ufscar.br",
                "rafaelaguardando",
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

        garantirAlunoNaOferta(alunoPlano, ofertaPrincipal, StatusAluno.NAO_ENVIADO, professorSupervisor);
        garantirAlunoNaOferta(alunoDocumentacao, ofertaPrincipal, StatusAluno.NAO_ENVIADO, professorSupervisor);
        garantirAlunoNaOferta(alunoRelatorio, ofertaPrincipal, StatusAluno.PLANO_ENVIADO, professorSupervisor);
        garantirAlunoNaOferta(alunoPlanoAprovado, ofertaPrincipal, StatusAluno.PLANO_APROVADO, professorSupervisor);
        garantirAlunoNaOferta(alunoDocEnviada, ofertaPrincipal, StatusAluno.DOCUMENTACAO_ENVIADA, professorSupervisor);
        garantirAlunoNaOferta(alunoRelatorioEnviado, ofertaPrincipal, StatusAluno.RELATORIO_ENVIADO, professorSupervisor);
        garantirAlunoNaOferta(alunoRelatorioAprovado, ofertaPrincipal, StatusAluno.RELATORIO_APROVADO_SUPERVISOR, professorSupervisor);

        Oferta ofertaConcluida = criarOuAtualizarOferta(
                OFERTA_CONCLUIDA_NOME,
                "2024/2",
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 7, 31),
                professorResponsavel2,
                StatusOferta.CONCLUIDA,
                secretario,
                secretario,
                LocalDateTime.now().minusDays(30),
                "Oferta concluída para teste.",
                "Somente leitura."
        );
        garantirAlunoNaOferta(alunoConcluido1, ofertaConcluida, StatusAluno.CONCLUIDO_PELO_RESPONSAVEL, professorSupervisor);
        garantirAlunoNaOferta(alunoConcluido2, ofertaConcluida, StatusAluno.CONCLUIDO_PELO_RESPONSAVEL, professorSupervisor);

        Oferta ofertaAguardando = criarOuAtualizarOferta(
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
        garantirAlunoNaOferta(alunoAguardando, ofertaAguardando, StatusAluno.CONCLUIDO_PELO_RESPONSAVEL, professorSupervisor);

        // Oferta em atraso permanece sem alunos matriculados (cenário de turma vazia).
        criarOuAtualizarOferta(
                OFERTA_ATRASADA_NOME,
                "2023/2",
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2023, 12, 15),
                professorResponsavel2,
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

        if (precisaDePlano(status)) {
            alunoOferta.setPlano(garantirPlanoMinimo(alunoOferta.getPlano(), professorSupervisor, status));
        }

        if (status == StatusAluno.DOCUMENTACAO_ENVIADA) {
            alunoOferta.setDocumentacao(garantirDocumentacaoMinima(alunoOferta.getDocumentacao()));
        }

        alunoOferta = alunoOfertaRepositorio.save(alunoOferta);

        if (status == StatusAluno.RELATORIO_ENVIADO
                || status == StatusAluno.RELATORIO_APROVADO_SUPERVISOR
                || status == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL) {
            garantirRelatorioMinimo(alunoOferta, status);
        }
    }

    private boolean precisaDePlano(StatusAluno status) {
        return status == StatusAluno.PLANO_ENVIADO
                || status == StatusAluno.PLANO_APROVADO
                || status == StatusAluno.RELATORIO_ENVIADO
                || status == StatusAluno.RELATORIO_APROVADO_SUPERVISOR
                || status == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL;
    }

    private PlanoTrabalho garantirPlanoMinimo(
            PlanoTrabalho planoExistente,
            Usuario professorSupervisor,
            StatusAluno statusAluno) {
        PlanoTrabalho plano = planoExistente != null
                ? planoExistente
                : new PlanoTrabalho();

        plano.setCodigoDisciplina("DC-001");
        plano.setNomeDisciplina("Estágio Supervisionado em Docência");
        plano.setCursoDisciplina("Computação");
        plano.setProfessorSupervisor(professorSupervisor);
        plano.setArquivoPath(PDF_PLANO_PEDRO);
        if (plano.getEnviadoEm() == null) {
            plano.setEnviadoEm(LocalDateTime.now().minusDays(10));
        }

        boolean planoAprovado = statusAluno == StatusAluno.PLANO_APROVADO
                || statusAluno == StatusAluno.RELATORIO_ENVIADO
                || statusAluno == StatusAluno.RELATORIO_APROVADO_SUPERVISOR
                || statusAluno == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL;

        if (planoAprovado) {
            plano.setParecer("Plano aprovado para testes.");
            if (plano.getAprovadoEm() == null) {
                plano.setAprovadoEm(LocalDateTime.now().minusDays(5));
            }
            plano.setAprovadoPor(professorSupervisor);
        } else {
            plano.setParecer(null);
            plano.setAprovadoEm(null);
            plano.setAprovadoPor(null);
        }

        return planoTrabalhoRepositorio.save(plano);
    }

    private DocumentacaoDocencia garantirDocumentacaoMinima(DocumentacaoDocencia documentacaoExistente) {
        DocumentacaoDocencia documentacao = documentacaoExistente != null
                ? documentacaoExistente
                : new DocumentacaoDocencia();

        documentacao.setNomeInstituicao("UFSCar");
        documentacao.setNomeDisciplina("Introdução à Programação");
        documentacao.setCursoDisciplina("Engenharia de Computação");
        documentacao.setCargaHoraria(60);
        documentacao.setArquivoPath(PDF_PLANO_PEDRO);
        if (documentacao.getEnviadaEm() == null) {
            documentacao.setEnviadaEm(LocalDateTime.now().minusDays(7));
        }

        return documentacaoDocenciaRepositorio.save(documentacao);
    }

    private void garantirRelatorioMinimo(AlunoOferta alunoOferta, StatusAluno statusAluno) {
        RelatorioEstagio relatorio = relatorioEstagioRepositorio.findByAlunoOferta(alunoOferta)
                .orElseGet(RelatorioEstagio::new);

        relatorio.setAlunoOferta(alunoOferta);
        relatorio.setArquivoPath(PDF_PLANO_PEDRO);
        relatorio.setIndicadorFrequencia(85);
        if (relatorio.getEnviadoEm() == null) {
            relatorio.setEnviadoEm(LocalDateTime.now().minusDays(3));
        }

        if (statusAluno == StatusAluno.RELATORIO_APROVADO_SUPERVISOR
                || statusAluno == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL) {
            relatorio.setParecerSupervisor("Relatório aprovado pelo supervisor para testes.");
            relatorio.setFrequenciaSupervisor(85.0);
            relatorio.setSugestaoNotaSupervisor(Nota.A);
            if (relatorio.getAprovadoPorSupervisorEm() == null) {
                relatorio.setAprovadoPorSupervisorEm(LocalDateTime.now().minusDays(2));
            }
        }

        if (statusAluno == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL) {
            relatorio.setParecerResponsavel("Relatório concluído pelo responsável para testes.");
            relatorio.setFrequenciaResponsavel(85.0);
            relatorio.setNotaResponsavel(Nota.A);
            relatorio.setAprovadoPorResponsavel(alunoOferta.getOferta().getProfessorResponsavel());
            if (relatorio.getAprovadoPorResponsavelEm() == null) {
                relatorio.setAprovadoPorResponsavelEm(LocalDateTime.now().minusDays(1));
            }
        }

        relatorioEstagioRepositorio.save(relatorio);
    }
}
