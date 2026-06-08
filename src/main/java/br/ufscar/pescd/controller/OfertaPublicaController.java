package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

@Controller
@RequestMapping("/ofertas")
@RequiredArgsConstructor
public class OfertaPublicaController {

    private final OfertaService ofertaService;

    // V.01
    @GetMapping
    public String listar(Model model) {
        Map<Oferta, Long> ofertasComContagem = ofertaService.listarPublicasComContagem();
        model.addAttribute("ofertasComContagem", ofertasComContagem);
        return "public/ofertas";
    }
}