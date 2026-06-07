package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.service.ProfessorService;
import br.ufscar.pescd.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor/ofertas")
@RequiredArgsConstructor
public class OfertaProfessorController {

    private final OfertaService ofertaService;
    private final ProfessorService professorService;

    @GetMapping("/{id}/encerrar")
    public String exibirFormularioEncerramento(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

        Oferta oferta = ofertaService.buscarPorId(id);
        List<AlunoOferta> alunos = ofertaService.buscarAlunosPorOferta(id);
        Map<Long, RelatorioEstagio> relatorios = professorService.buscarRelatoriosDaOferta(id);

        // VALIDAÇÃO PR.03: Bloqueia a entrada na tela se houver aluno não concluído
        boolean todosConcluidos = !alunos.isEmpty() && alunos.stream()
                .allMatch(aluno -> aluno.getStatus() == br.ufscar.pescd.entity.enums.StatusAluno.CONCLUIDO_PELO_RESPONSAVEL);

        if (!todosConcluidos) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Não é possível encerrar a oferta. Todos os alunos inscritos devem estar no status 'Concluído pelo responsável'.");
            return "redirect:/professor";
        }

        model.addAttribute("oferta", oferta);
        model.addAttribute("alunos", alunos);
        model.addAttribute("relatorios", relatorios);

        return "professor/formEncerramento";
    }

    @PostMapping("/{id}/encerrar")
    public String solicitarEncerramento(
            @PathVariable Long id,
            @RequestParam String licoesAprendidas,
            @RequestParam String instrucaoEncerramento,
            RedirectAttributes redirectAttributes) {

        try {

            ofertaService.solicitarEncerramento(
                    id,
                    licoesAprendidas,
                    instrucaoEncerramento
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Solicitação de encerramento enviada com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    "Erro ao solicitar encerramento: " + e.getMessage()
            );
        }

        return "redirect:/professor";
    }
}