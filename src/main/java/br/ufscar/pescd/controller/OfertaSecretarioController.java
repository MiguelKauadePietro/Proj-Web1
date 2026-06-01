package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.service.OfertaService;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/secretario/ofertas")
@RequiredArgsConstructor
public class OfertaSecretarioController {

    private final OfertaService ofertaService;
    private final UsuarioRepositorio usuarioRepositorio;

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("oferta", new Oferta());

        model.addAttribute("professores", usuarioRepositorio.findAll());

        return "secretario/formOferta";
    }

    @PostMapping("/salvar")
    public String salvarOferta(Oferta oferta) {
        try {
            ofertaService.salvar(oferta);
            return "redirect:/ofertas";
        } catch (IllegalArgumentException e) {
            return "secretario/formOferta";
        }
    }
}