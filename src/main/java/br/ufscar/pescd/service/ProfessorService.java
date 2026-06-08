package br.ufscar.pescd.service;

import br.ufscar.pescd.dto.AprovacaoPlanoFormDto;
import br.ufscar.pescd.dto.AprovacaoRelatorioFormDto;
import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.DocumentacaoDocencia;
import br.ufscar.pescd.entity.LogStatusAluno;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.PlanoTrabalho;
import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.entity.enums.StatusAluno;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.exception.AcaoNaoPermitidaException;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.DocumentacaoDocenciaRepositorio;
import br.ufscar.pescd.repository.LogStatusAlunoRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import br.ufscar.pescd.repository.PlanoTrabalhoRepositorio;
import br.ufscar.pescd.repository.RelatorioEstagioRepositorio;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final AlunoOfertaRepositorio alunoOfertaRepositorio;
    private final LogStatusAlunoRepositorio logStatusAlunoRepositorio;
    private final PlanoTrabalhoRepositorio planoTrabalhoRepositorio;
    private final RelatorioEstagioRepositorio relatorioEstagioRepositorio;
    private final DocumentacaoDocenciaRepositorio documentacaoDocenciaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final OfertaRepositorio ofertaRepositorio;

    @Transactional(readOnly = true)
    public List<AlunoOferta> listarAlunosSupervisionados(String username) {
        Usuario professor = buscarProfessorAtivo(username);
        return alunoOfertaRepositorio.findByProfessorSupervisor(professor);
    }

    @Transactional(readOnly = true)
    public List<AlunoOferta> listarAlunosComoResponsavel(String username) {
        Usuario professor = buscarProfessorAtivo(username);
        return alunoOfertaRepositorio.findByProfessorResponsavel(professor);
    }

    @Transactional(readOnly = true)
    public List<Oferta> listarOfertasComoResponsavel(String username) {
        Usuario professor = buscarProfessorAtivo(username);
        return ofertaRepositorio.findByProfessorResponsavel(professor);
    }

    @Transactional(readOnly = true)
    public AlunoOferta buscarAlunoOferta(Long id) {
        return alunoOfertaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));
    }

    @Transactional(readOnly = true)
    public AlunoOferta buscarAlunoOfertaComoSupervisor(Long id, String username) {
        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOferta(id);

        if (alunoOferta.getPlano() == null || !professor.equals(alunoOferta.getPlano().getProfessorSupervisor())) {
            throw new AccessDeniedException("Você não é o professor supervisor deste aluno.");
        }

        return alunoOferta;
    }

    @Transactional(readOnly = true)
    public AlunoOferta buscarAlunoOfertaComoResponsavel(Long id, String username) {
        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOferta(id);

        if (alunoOferta.getOferta().getProfessorResponsavel() == null
                || !professor.equals(alunoOferta.getOferta().getProfessorResponsavel())) {
            throw new AccessDeniedException("Você não é o professor responsável desta oferta.");
        }

        return alunoOferta;
    }

    @Transactional(readOnly = true)
    public Oferta buscarOferta(Long id) {
        return ofertaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Oferta não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<AlunoOferta> listarAlunosDaOferta(Long ofertaId) {
        Oferta oferta = buscarOferta(ofertaId);
        return alunoOfertaRepositorio.findByOferta(oferta);
    }

    @Transactional(readOnly = true)
    public Map<Long, RelatorioEstagio> buscarRelatoriosDaOferta(Long ofertaId) {
        Oferta oferta = buscarOferta(ofertaId);
        List<AlunoOferta> alunos = alunoOfertaRepositorio.findByOferta(oferta);
        Map<Long, RelatorioEstagio> map = new HashMap<>();
        for (AlunoOferta ao : alunos) {
            relatorioEstagioRepositorio.findByAlunoOferta(ao)
                    .ifPresent(r -> map.put(ao.getId(), r));
        }
        return map;
    }

    @Transactional(readOnly = true)
    public RelatorioEstagio buscarRelatorio(Long alunoOfertaId) {
        AlunoOferta alunoOferta = buscarAlunoOferta(alunoOfertaId);
        return relatorioEstagioRepositorio.findByAlunoOferta(alunoOferta).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean podeEncerrarOferta(Long ofertaId, String username) {
        Usuario professor = buscarProfessorAtivo(username);
        Oferta oferta = buscarOferta(ofertaId);
        if (!professor.equals(oferta.getProfessorResponsavel())) {
            return false;
        }

        if (oferta.getStatus() != StatusOferta.EM_ANDAMENTO && oferta.getStatus() != StatusOferta.EM_ATRASO) {
            return false;
        }

        List<AlunoOferta> alunos = alunoOfertaRepositorio.findByOferta(oferta);
        return alunos.stream().allMatch(aluno -> aluno.getStatus() == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL);
    }

    @Transactional(readOnly = true)
    public String buscarArquivoDoProfessor(Long alunoOfertaId, String username, String tipo) {
        String tipoNormalizado = tipo == null ? "" : tipo.trim().toLowerCase();
        AlunoOferta alunoOferta = switch (tipoNormalizado) {
            case "plano", "relatorio" -> buscarAlunoOfertaComoSupervisorOuResponsavel(alunoOfertaId, username);
            case "documentacao" -> buscarAlunoOfertaComoResponsavel(alunoOfertaId, username);
            default -> throw new EntityNotFoundException("Tipo de arquivo não encontrado.");
        };

        return switch (tipoNormalizado) {
            case "plano" -> buscarPlanoObrigatorio(alunoOferta).getArquivoPath();
            case "relatorio" -> buscarRelatorioObrigatorio(alunoOferta).getArquivoPath();
            case "documentacao" -> buscarDocumentacaoObrigatoria(alunoOferta).getArquivoPath();
            default -> throw new EntityNotFoundException("Tipo de arquivo não encontrado.");
        };
    }

    @Transactional
    public void aprovarPlano(Long alunoOfertaId,
                             String username,
                             AprovacaoPlanoFormDto form) {

        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOfertaComoSupervisor(alunoOfertaId, username);
        validarOfertaEditavel(alunoOferta.getOferta());

        if (alunoOferta.getStatus() != StatusAluno.PLANO_ENVIADO) {
            throw new AcaoNaoPermitidaException("O plano precisa estar enviado.");
        }

        PlanoTrabalho plano = alunoOferta.getPlano();
        plano.setParecer(form.getParecer());
        plano.setAprovadoEm(LocalDateTime.now());
        plano.setAprovadoPor(professor);
        planoTrabalhoRepositorio.save(plano);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setStatus(StatusAluno.PLANO_APROVADO);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.PLANO_APROVADO, professor, form.getParecer());
    }

    @Transactional
    public void aprovarRelatorio(Long alunoOfertaId,
                                 String username,
                                 AprovacaoRelatorioFormDto form) {

        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOfertaComoSupervisor(alunoOfertaId, username);
        validarOfertaEditavel(alunoOferta.getOferta());

        if (alunoOferta.getStatus() != StatusAluno.RELATORIO_ENVIADO) {
            throw new AcaoNaoPermitidaException("O relatório precisa estar enviado.");
        }

        RelatorioEstagio relatorio = relatorioEstagioRepositorio
                .findByAlunoOferta(alunoOferta)
                .orElseThrow(() -> new EntityNotFoundException("Relatório não encontrado."));

        relatorio.setParecerSupervisor(form.getParecer());
        relatorio.setFrequenciaSupervisor(form.getFrequencia());
        relatorio.setSugestaoNotaSupervisor(form.getSugestaoNota());
        relatorio.setAprovadoPorSupervisorEm(LocalDateTime.now());
        relatorioEstagioRepositorio.save(relatorio);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setStatus(StatusAluno.RELATORIO_APROVADO_SUPERVISOR);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.RELATORIO_APROVADO_SUPERVISOR, professor, form.getParecer());
    }

    @Transactional
    public void concluirRelatorioResponsavel(Long alunoOfertaId,
                                             String username,
                                             AprovacaoRelatorioFormDto form) {

        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOfertaComoResponsavel(alunoOfertaId, username);
        validarOfertaEditavel(alunoOferta.getOferta());

        if (alunoOferta.getStatus() != StatusAluno.RELATORIO_APROVADO_SUPERVISOR) {
            throw new AcaoNaoPermitidaException("O relatório precisa estar aprovado pelo supervisor.");
        }

        RelatorioEstagio relatorio = relatorioEstagioRepositorio
                .findByAlunoOferta(alunoOferta)
                .orElseThrow(() -> new EntityNotFoundException("Relatório não encontrado."));

        relatorio.setParecerResponsavel(form.getParecer());
        relatorio.setFrequenciaResponsavel(form.getFrequencia());
        relatorio.setNotaResponsavel(form.getSugestaoNota());
        relatorio.setAprovadoPorResponsavelEm(LocalDateTime.now());
        relatorio.setAprovadoPorResponsavel(professor);
        relatorioEstagioRepositorio.save(relatorio);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setStatus(StatusAluno.CONCLUIDO_PELO_RESPONSAVEL);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.CONCLUIDO_PELO_RESPONSAVEL, professor, form.getParecer());
    }

    @Transactional
    public void analisarDocumentacao(Long alunoOfertaId,
                                     String username,
                                     AprovacaoRelatorioFormDto form) {

        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOfertaComoResponsavel(alunoOfertaId, username);
        validarOfertaEditavel(alunoOferta.getOferta());

        if (alunoOferta.getStatus() != StatusAluno.DOCUMENTACAO_ENVIADA) {
            throw new AcaoNaoPermitidaException("A documentação precisa estar enviada.");
        }

        DocumentacaoDocencia doc = alunoOferta.getDocumentacao();
        if (doc == null) {
            throw new EntityNotFoundException("Documentação não encontrada.");
        }

        doc.setParecer(form.getParecer());
        doc.setFrequencia(form.getFrequencia());
        doc.setNota(form.getSugestaoNota());
        doc.setAnalisadaEm(LocalDateTime.now());
        doc.setAnalisadaPor(professor);
        documentacaoDocenciaRepositorio.save(doc);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setStatus(StatusAluno.CONCLUIDO_PELO_RESPONSAVEL);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.CONCLUIDO_PELO_RESPONSAVEL, professor, form.getParecer());
    }

    @Transactional
    public void encerrarOferta(Long ofertaId, String username, String licoesAprendidas, String instrucaoEncerramento) {
        Usuario professor = buscarProfessorAtivo(username);
        Oferta oferta = buscarOferta(ofertaId);

        if (!oferta.getProfessorResponsavel().equals(professor)) {
            throw new AcaoNaoPermitidaException("Você não é o responsável por esta oferta.");
        }

        if (oferta.getStatus() != StatusOferta.EM_ANDAMENTO && oferta.getStatus() != StatusOferta.EM_ATRASO) {
            throw new AcaoNaoPermitidaException("A oferta precisa estar em andamento ou em atraso para ser encerrada.");
        }

        List<AlunoOferta> alunos = alunoOfertaRepositorio.findByOferta(oferta);
        boolean todosCompletos = alunos.stream()
                .allMatch(a -> a.getStatus() == StatusAluno.CONCLUIDO_PELO_RESPONSAVEL);

        if (!todosCompletos) {
            throw new AcaoNaoPermitidaException("Todos os alunos precisam estar com status 'Concluído pelo responsável'.");
        }

        oferta.setStatus(StatusOferta.AGUARDANDO_ENCERRAMENTO);
        oferta.setLicoesAprendidas(licoesAprendidas);
        oferta.setInstrucaoEncerramento(instrucaoEncerramento);
        oferta.setEncerradoPor(professor);
        oferta.setEncerradoEm(LocalDateTime.now());
        ofertaRepositorio.save(oferta);
    }

    private AlunoOferta buscarAlunoOfertaComoSupervisorOuResponsavel(Long id, String username) {
        Usuario professor = buscarProfessorAtivo(username);
        AlunoOferta alunoOferta = buscarAlunoOferta(id);
        boolean supervisor = alunoOferta.getPlano() != null && professor.equals(alunoOferta.getPlano().getProfessorSupervisor());
        boolean responsavel = professor.equals(alunoOferta.getOferta().getProfessorResponsavel());

        if (!supervisor && !responsavel) {
            throw new AccessDeniedException("Você não tem permissão para acessar este arquivo.");
        }

        return alunoOferta;
    }

    private PlanoTrabalho buscarPlanoObrigatorio(AlunoOferta alunoOferta) {
        if (alunoOferta.getPlano() == null) {
            throw new EntityNotFoundException("Plano não encontrado.");
        }
        return alunoOferta.getPlano();
    }

    private RelatorioEstagio buscarRelatorioObrigatorio(AlunoOferta alunoOferta) {
        return relatorioEstagioRepositorio.findByAlunoOferta(alunoOferta)
                .orElseThrow(() -> new EntityNotFoundException("Relatório não encontrado."));
    }

    private DocumentacaoDocencia buscarDocumentacaoObrigatoria(AlunoOferta alunoOferta) {
        if (alunoOferta.getDocumentacao() == null) {
            throw new EntityNotFoundException("Documentação não encontrada.");
        }
        return alunoOferta.getDocumentacao();
    }

    private void validarOfertaEditavel(Oferta oferta) {
        if (oferta.getStatus() != StatusOferta.EM_ANDAMENTO && oferta.getStatus() != StatusOferta.EM_ATRASO) {
            throw new AcaoNaoPermitidaException("Esta oferta não está em um status que permite alterações.");
        }
    }

    private void registrarLog(AlunoOferta alunoOferta,
                              StatusAluno statusAnterior,
                              StatusAluno statusNovo,
                              Usuario alteradoPor,
                              String observacao) {

        LogStatusAluno log = new LogStatusAluno();
        log.setAlunoOferta(alunoOferta);
        log.setStatusAnterior(statusAnterior);
        log.setStatusNovo(statusNovo);
        log.setAlteradoEm(LocalDateTime.now());
        log.setAlteradoPor(alteradoPor);
        log.setObservacao(observacao);
        logStatusAlunoRepositorio.save(log);
    }

    private Usuario buscarProfessorAtivo(String username) {
        Usuario usuario = usuarioRepositorio.findByUsernameAndAtivoTrue(username)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado."));

        if (usuario.getPerfil() != Perfil.PROFESSOR) {
            throw new EntityNotFoundException("Professor não encontrado.");
        }

        return usuario;
    }
}
