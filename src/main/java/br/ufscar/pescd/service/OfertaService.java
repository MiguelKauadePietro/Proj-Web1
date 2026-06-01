package br.ufscar.pescd.service;

import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.enums.StatusOferta;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // REGRA 1: Se o secretário não digitar um nome, geramos um automático
        if (oferta.getNome() == null || oferta.getNome().trim().isEmpty()) {
            oferta.setNome("Oferta - " + oferta.getSemestre());
        }

        // REGRA 2: Validação das datas (A data de fim deve ser depois da data de início)
        if (oferta.getDataInicio() != null && oferta.getDataFim() != null) {
            if (oferta.getDataFim().isBefore(oferta.getDataInicio())) {
                throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
            }
        }

        // REGRA 3: Auditoria (Grava o momento exato em que a oferta foi criada)
        oferta.setCriadoEm(LocalDateTime.now());

        // REGRA 4: Status Inicial (Garante que ela comece EM_ANDAMENTO)
        oferta.setStatus(StatusOferta.EM_ANDAMENTO);

        // Por fim, salva no banco de dados
        ofertaRepositorio.save(oferta);
    }
}