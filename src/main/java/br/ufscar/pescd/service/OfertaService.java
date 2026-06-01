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


    // NOVOS MÉTODOS PARA A USER STORY S.02


    // Buscar uma oferta pelo ID
    public Oferta buscarPorId(Long id) {
        return ofertaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada com o ID: " + id));
    }

    // Buscar a lista de alunos matriculados em uma oferta específica
    public List<AlunoOferta> buscarAlunosPorOferta(Long ofertaId) {
        Oferta oferta = buscarPorId(ofertaId);
        return alunoOfertaRepositorio.findByOferta(oferta);
    }

    // Matricular um aluno na oferta (Evitando duplicados)
    @Transactional
    public void matricularAlunoNaOferta(Long ofertaId, Usuario aluno) {
        Oferta oferta = buscarPorId(ofertaId);

        // Verifica se o aluno já está matriculado nesta oferta para não duplicar
        boolean jaMatriculado = alunoOfertaRepositorio.existsByOfertaAndAluno(oferta, aluno);
        if (jaMatriculado) {
            throw new IllegalArgumentException("Este aluno já está matriculado nesta oferta.");
        }

        // Cria o registro de vínculo
        AlunoOferta alunoOferta = new AlunoOferta();
        alunoOferta.setOferta(oferta);
        alunoOferta.setAluno(aluno);
        alunoOferta.setStatus(StatusAluno.NAO_ENVIADO); // Status inicial exigido pelo modelo

        alunoOfertaRepositorio.save(alunoOferta);
    }

    // Remover a matrícula de um aluno da oferta
    @Transactional
    public void removerAlunoDaOferta(Long alunoOfertaId) {
        if (!alunoOfertaRepositorio.existsById(alunoOfertaId)) {
            throw new RuntimeException("Registro de matrícula não encontrado.");
        }
        alunoOfertaRepositorio.deleteById(alunoOfertaId);
    }
}