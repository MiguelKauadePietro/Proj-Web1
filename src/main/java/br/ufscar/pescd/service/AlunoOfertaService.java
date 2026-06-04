package br.ufscar.pescd.service;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.DocumentacaoDocencia;
import br.ufscar.pescd.entity.LogStatusAluno;
import br.ufscar.pescd.entity.PlanoTrabalho;
import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.entity.enums.StatusAluno;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.repository.DocumentacaoDocenciaRepositorio;
import br.ufscar.pescd.repository.PlanoTrabalhoRepositorio;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.LogStatusAlunoRepositorio;
import br.ufscar.pescd.repository.RelatorioEstagioRepositorio;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import br.ufscar.pescd.dto.DocumentacaoDocenciaFormDto;
import br.ufscar.pescd.dto.PlanoTrabalhoFormDto;
import br.ufscar.pescd.dto.RelatorioFinalFormDto;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoOfertaService {

    private final AlunoOfertaRepositorio alunoOfertaRepositorio;
    private final LogStatusAlunoRepositorio logStatusAlunoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PlanoTrabalhoRepositorio planoTrabalhoRepositorio;
    private final DocumentacaoDocenciaRepositorio documentacaoDocenciaRepositorio;
    private final RelatorioEstagioRepositorio relatorioEstagioRepositorio;
    private final ArquivoStorageService arquivoStorageService;

    @Transactional(readOnly = true)
    public List<AlunoOferta> listarDoAluno(String username) {
        Usuario aluno = buscarAlunoAtivo(username);
        return alunoOfertaRepositorio.findByAlunoOrdenado(aluno);
    }

    @Transactional(readOnly = true)
    public AlunoOferta buscarDoAluno(Long alunoOfertaId, String username) {
        Usuario aluno = buscarAlunoAtivo(username);
        return alunoOfertaRepositorio.findByIdAndAluno(alunoOfertaId, aluno)
                .orElseThrow(() -> new EntityNotFoundException("Oferta do aluno não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<LogStatusAluno> listarHistorico(Long alunoOfertaId, String username) {
        AlunoOferta alunoOferta = buscarDoAluno(alunoOfertaId, username);
        return logStatusAlunoRepositorio.findByAlunoOfertaOrderByAlteradoEmDesc(alunoOferta);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarProfessores() {
        return usuarioRepositorio.findByPerfilInAndAtivoTrue(List.of(Perfil.PROFESSOR));
    }

    @Transactional(readOnly = true)
    public void validarDisponibilidadeEnvioPlano(Long alunoOfertaId, String username) {
        validarEnvioPlano(buscarDoAluno(alunoOfertaId, username));
    }

    @Transactional(readOnly = true)
    public void validarDisponibilidadeEnvioDocumentacao(Long alunoOfertaId, String username) {
        validarEnvioPlano(buscarDoAluno(alunoOfertaId, username));
    }

    @Transactional(readOnly = true)
    public void validarDisponibilidadeEnvioRelatorio(Long alunoOfertaId, String username) {
        validarEnvioRelatorio(buscarDoAluno(alunoOfertaId, username));
    }

    @Transactional
    public void enviarPlano(Long alunoOfertaId, String username, PlanoTrabalhoFormDto form) {
        Usuario aluno = buscarAlunoAtivo(username);
        AlunoOferta alunoOferta = alunoOfertaRepositorio.findByIdAndAluno(alunoOfertaId, aluno)
                .orElseThrow(() -> new EntityNotFoundException("Oferta do aluno não encontrada."));

        validarEnvioPlano(alunoOferta);

        Usuario professorSupervisor = usuarioRepositorio.findById(form.getProfessorSupervisorId())
                .filter(usuario -> usuario.getPerfil() == Perfil.PROFESSOR)
                .orElseThrow(() -> new IllegalArgumentException("Professor supervisor inválido."));

        String arquivoPath = arquivoStorageService.salvarPdf(form.getArquivo(), "planos");

        PlanoTrabalho planoTrabalho = new PlanoTrabalho();
        planoTrabalho.setCodigoDisciplina(form.getCodigoDisciplina().trim());
        planoTrabalho.setNomeDisciplina(form.getNomeDisciplina().trim());
        planoTrabalho.setCursoDisciplina(form.getCursoDisciplina().trim());
        planoTrabalho.setProfessorSupervisor(professorSupervisor);
        planoTrabalho.setArquivoPath(arquivoPath);
        planoTrabalho.setEnviadoEm(LocalDateTime.now());
        planoTrabalho = planoTrabalhoRepositorio.save(planoTrabalho);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setPlano(planoTrabalho);
        alunoOferta.setStatus(StatusAluno.PLANO_ENVIADO);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.PLANO_ENVIADO, aluno, null);
    }

    @Transactional
    public void enviarDocumentacao(Long alunoOfertaId, String username, DocumentacaoDocenciaFormDto form) {
        Usuario aluno = buscarAlunoAtivo(username);
        AlunoOferta alunoOferta = alunoOfertaRepositorio.findByIdAndAluno(alunoOfertaId, aluno)
                .orElseThrow(() -> new EntityNotFoundException("Oferta do aluno não encontrada."));

        validarEnvioPlano(alunoOferta);

        String arquivoPath = arquivoStorageService.salvarPdf(form.getArquivo(), "documentacoes");

        DocumentacaoDocencia documentacao = new DocumentacaoDocencia();
        documentacao.setNomeInstituicao(form.getNomeInstituicao().trim());
        documentacao.setNomeDisciplina(form.getNomeDisciplina().trim());
        documentacao.setCursoDisciplina(form.getCursoDisciplina().trim());
        documentacao.setCargaHoraria(form.getCargaHoraria());
        documentacao.setArquivoPath(arquivoPath);
        documentacao.setEnviadaEm(LocalDateTime.now());
        documentacao = documentacaoDocenciaRepositorio.save(documentacao);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setDocumentacao(documentacao);
        alunoOferta.setStatus(StatusAluno.DOCUMENTACAO_ENVIADA);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.DOCUMENTACAO_ENVIADA, aluno, null);
    }

    @Transactional(readOnly = true)
    public RelatorioEstagio buscarRelatorio(Long alunoOfertaId, String username) {
        AlunoOferta alunoOferta = buscarDoAluno(alunoOfertaId, username);
        return relatorioEstagioRepositorio.findByAlunoOferta(alunoOferta).orElse(null);
    }

    @Transactional
    public void enviarRelatorioFinal(Long alunoOfertaId, String username, RelatorioFinalFormDto form) {
        Usuario aluno = buscarAlunoAtivo(username);
        AlunoOferta alunoOferta = alunoOfertaRepositorio.findByIdAndAluno(alunoOfertaId, aluno)
                .orElseThrow(() -> new EntityNotFoundException("Oferta do aluno não encontrada."));

        validarEnvioRelatorio(alunoOferta);

        String arquivoPath = arquivoStorageService.salvarPdf(form.getArquivo(), "relatorios");

        RelatorioEstagio relatorioEstagio = relatorioEstagioRepositorio.findByAlunoOferta(alunoOferta)
                .orElse(new RelatorioEstagio());
        relatorioEstagio.setAlunoOferta(alunoOferta);
        relatorioEstagio.setArquivoPath(arquivoPath);
        relatorioEstagio.setIndicadorFrequencia(form.getIndicadorFrequencia());
        relatorioEstagio.setEnviadoEm(LocalDateTime.now());
        relatorioEstagioRepositorio.save(relatorioEstagio);

        StatusAluno statusAnterior = alunoOferta.getStatus();
        alunoOferta.setStatus(StatusAluno.RELATORIO_ENVIADO);
        alunoOfertaRepositorio.save(alunoOferta);

        registrarLog(alunoOferta, statusAnterior, StatusAluno.RELATORIO_ENVIADO, aluno, null);
    }

    private void validarEnvioPlano(AlunoOferta alunoOferta) {
        if (alunoOferta.getOferta().getStatus() != StatusOferta.EM_ANDAMENTO) {
            throw new IllegalStateException("O plano só pode ser enviado para ofertas em andamento.");
        }

        if (alunoOferta.getStatus() != StatusAluno.NAO_ENVIADO) {
            throw new IllegalStateException("O plano só pode ser enviado quando o status do aluno for Não enviado.");
        }
    }

    private void validarEnvioRelatorio(AlunoOferta alunoOferta) {
        if (alunoOferta.getOferta().getStatus() != StatusOferta.EM_ANDAMENTO) {
            throw new IllegalStateException("O relatório só pode ser enviado para ofertas em andamento.");
        }

        if (alunoOferta.getStatus() != StatusAluno.PLANO_APROVADO) {
            throw new IllegalStateException("O relatório final só pode ser enviado quando o plano estiver aprovado.");
        }

        if (alunoOferta.getPlano() == null) {
            throw new IllegalStateException("Não há plano de trabalho associado a esta oferta.");
        }
    }

    private void registrarLog(
            AlunoOferta alunoOferta,
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

    private Usuario buscarAlunoAtivo(String username) {
        Usuario usuario = usuarioRepositorio.findByUsernameAndAtivoTrue(username)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        if (usuario.getPerfil() != Perfil.ALUNO) {
            throw new EntityNotFoundException("Aluno não encontrado.");
        }

        return usuario;
    }
}
