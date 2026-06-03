package br.ufscar.pescd.service;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.StatusAluno;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OfertaService {

    private final OfertaRepositorio ofertaRepositorio;
    private final AlunoOfertaRepositorio alunoOfertaRepositorio;

    public Map<Oferta, Long> listarPublicasComContagem() {
        List<Oferta> ofertas = ofertaRepositorio.findAllByOrderBySemestreDesc();
        Map<Oferta, Long> resultado = new LinkedHashMap<>();
        for (Oferta oferta : ofertas) {
            resultado.put(oferta, alunoOfertaRepositorio.countByOferta(oferta));
        }
        return resultado;
    }

    public void salvar(Oferta oferta) {
        if (oferta.getNome() == null || oferta.getNome().trim().isEmpty()) {
            oferta.setNome("Oferta - " + oferta.getSemestre());
        }

        if (oferta.getDataInicio() != null && oferta.getDataFim() != null) {
            if (oferta.getDataFim().isBefore(oferta.getDataInicio())) {
                throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
            }
        }

        oferta.setCriadoEm(LocalDateTime.now());
        oferta.setStatus(StatusOferta.EM_ANDAMENTO);
        ofertaRepositorio.save(oferta);
    }



    public Oferta buscarPorId(Long id) {
        return ofertaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada com o ID: " + id));
    }

    public List<AlunoOferta> buscarAlunosPorOferta(Long ofertaId) {
        Oferta oferta = buscarPorId(ofertaId);
        return alunoOfertaRepositorio.findByOferta(oferta);
    }

    @Transactional
    public void matricularAlunoNaOferta(Long ofertaId, Usuario aluno) {
        Oferta oferta = buscarPorId(ofertaId);

        boolean jaMatriculado = alunoOfertaRepositorio.existsByOfertaAndAluno(oferta, aluno);
        if (jaMatriculado) {
            throw new IllegalArgumentException("Este aluno já está matriculado nesta oferta.");
        }

        AlunoOferta alunoOferta = new AlunoOferta();
        alunoOferta.setOferta(oferta);
        alunoOferta.setAluno(aluno);
        alunoOferta.setStatus(StatusAluno.NAO_ENVIADO);

        alunoOfertaRepositorio.save(alunoOferta);
    }

    @Transactional
    public void removerAlunoDaOferta(Long alunoOfertaId) {
        if (!alunoOfertaRepositorio.existsById(alunoOfertaId)) {
            throw new RuntimeException("Registro de matrícula não encontrado.");
        }
        alunoOfertaRepositorio.deleteById(alunoOfertaId);
    }

    @Transactional
    public void solicitarEncerramento(Long id, String licoes, String instrucoes) {
        Oferta oferta = buscarPorId(id);

        if (oferta.getStatus() != StatusOferta.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas ofertas EM_ANDAMENTO podem solicitar encerramento.");
        }

        if (licoes == null || licoes.trim().isEmpty() || instrucoes == null || instrucoes.trim().isEmpty()) {
            throw new IllegalArgumentException("As lições aprendidas e instruções de encerramento são obrigatórias.");
        }

        oferta.setLicoesAprendidas(licoes);
        oferta.setInstrucaoEncerramento(instrucoes);
        oferta.setStatus(StatusOferta.AGUARDANDO_ENCERRAMENTO);

        ofertaRepositorio.save(oferta);
    }


    @Transactional
    public void homologarEncerramento(Long id, Usuario secretario) {
        Oferta oferta = buscarPorId(id);

        if (oferta.getStatus() != StatusOferta.AGUARDANDO_ENCERRAMENTO) {
            throw new IllegalStateException("Apenas ofertas em estado AGUARDANDO_ENCERRAMENTO podem ser finalizadas.");
        }

        oferta.setStatus(StatusOferta.CONCLUIDA);
        oferta.setEncerradoPor(secretario);
        oferta.setEncerradoEm(LocalDateTime.now());

        ofertaRepositorio.save(oferta);
    }
}