package br.ufscar.pescd.service;

import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.repository.AlunoOfertaRepositorio;
import br.ufscar.pescd.repository.OfertaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OfertaService {

    private final OfertaRepositorio ofertaRepositorio;
    private final AlunoOfertaRepositorio alunoOfertaRepositorio;

    public List<Oferta> listarPublicas() {
        return ofertaRepositorio.findAllByOrderBySemestreDesc();
    }

    public Map<Oferta, Long> listarPublicasComContagem() {
        List<Oferta> ofertas = ofertaRepositorio.findAllByOrderBySemestreDesc();
        Map<Oferta, Long> resultado = new LinkedHashMap<>();
        for (Oferta oferta : ofertas) {
            resultado.put(oferta, alunoOfertaRepositorio.countByOferta(oferta));
        }
        return resultado;
    }
}
