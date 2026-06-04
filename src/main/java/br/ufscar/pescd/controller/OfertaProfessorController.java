package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/professor/ofertas")
@RequiredArgsConstructor
public class OfertaProfessorController {

    private final OfertaService ofertaService;

    @GetMapping
    public String listarOfertasProfessor() {
        return "redirect:/ofertas";
    }

    @GetMapping("/{id}/encerrar")
    public String exibirFormularioEncerramento(@PathVariable Long id, Model model) {
        Oferta oferta = ofertaService.buscarPorId(id);
        model.addAttribute("oferta", oferta);
        return "professor/formEncerramento";
    }

    @PostMapping("/{id}/encerrar")
    public String solicitarEncerramento(
            @PathVariable Long id,
            @RequestParam String licoesAprendidas,
            @RequestParam String instrucaoEncerramento,
            RedirectAttributes redirectAttributes) {
        try {
            ofertaService.solicitarEncerramento(id, licoesAprendidas, instrucaoEncerramento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Solicitação de encerramento enviada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao solicitar encerramento: " + e.getMessage());
        }
        return "redirect:/ofertas";
    }
}
