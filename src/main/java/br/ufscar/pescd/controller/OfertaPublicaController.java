package br.ufscar.pescd.controller;

import br.ufscar.pescd.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ofertas")
@RequiredArgsConstructor
public class OfertaPublicaController {

    private final OfertaService ofertaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ofertasComContagem", ofertaService.listarPublicasComContagem());
        return "public/ofertas";
    }
}
