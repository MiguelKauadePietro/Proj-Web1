package br.ufscar.pescd.controller;

import br.ufscar.pescd.dto.AprovacaoPlanoFormDto;
import br.ufscar.pescd.dto.AprovacaoRelatorioFormDto;
import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public String listar(Principal principal, Model model) {
        List<AlunoOferta> supervisionados =
                professorService.listarAlunosSupervisionados(principal.getName());
        List<AlunoOferta> responsavel =
                professorService.listarAlunosComoResponsavel(principal.getName());
        List<Oferta> ofertas =
                professorService.listarOfertasComoResponsavel(principal.getName());

        model.addAttribute("alunosSupervisionados", supervisionados);
        model.addAttribute("alunosResponsavel", responsavel);
        model.addAttribute("ofertas", ofertas);
        model.addAttribute(
                "ofertasEncerraveis",
                ofertas.stream()
                        .filter(oferta -> professorService.podeEncerrarOferta(oferta.getId(), principal.getName()))
                        .map(Oferta::getId)
                        .toList());

        return "professor/lista";
    }

    // PS.02 - Aprovar Plano
    @GetMapping("/{alunoOfertaId}/aprovar-plano")
    public String exibirAprovacaoPlano(@PathVariable Long alunoOfertaId, Principal principal, Model model) {
        AlunoOferta alunoOferta = professorService.buscarAlunoOfertaComoSupervisor(alunoOfertaId, principal.getName());
        model.addAttribute("alunoOferta", alunoOferta);
        model.addAttribute("form", new AprovacaoPlanoFormDto());
        return "professor/aprovarPlano";
    }

    @PostMapping("/{alunoOfertaId}/aprovar-plano")
    public String aprovarPlano(@PathVariable Long alunoOfertaId,
                               @ModelAttribute("form") AprovacaoPlanoFormDto form,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        try {
            professorService.aprovarPlano(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Plano aprovado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/professor";
    }

    // PS.03 - Aprovar Relatório (Supervisor)
    @GetMapping("/{alunoOfertaId}/aprovar-relatorio")
    public String exibirAprovacaoRelatorio(@PathVariable Long alunoOfertaId, Principal principal, Model model) {
        AlunoOferta alunoOferta = professorService.buscarAlunoOfertaComoSupervisor(alunoOfertaId, principal.getName());
        model.addAttribute("alunoOferta", alunoOferta);
        model.addAttribute("relatorio", professorService.buscarRelatorio(alunoOfertaId));
        model.addAttribute("form", new AprovacaoRelatorioFormDto());
        return "professor/aprovarRelatorio";
    }

    @PostMapping("/{alunoOfertaId}/aprovar-relatorio")
    public String aprovarRelatorio(@PathVariable Long alunoOfertaId,
                                   @ModelAttribute("form") AprovacaoRelatorioFormDto form,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        try {
            professorService.aprovarRelatorio(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Relatório aprovado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/professor";
    }

    // PR.01 - Concluir Relatório (Responsável)
    @GetMapping("/{alunoOfertaId}/concluir-relatorio")
    public String exibirConclusaoRelatorio(@PathVariable Long alunoOfertaId, Principal principal, Model model) {
        AlunoOferta alunoOferta = professorService.buscarAlunoOfertaComoResponsavel(alunoOfertaId, principal.getName());
        model.addAttribute("alunoOferta", alunoOferta);
        model.addAttribute("relatorio", professorService.buscarRelatorio(alunoOfertaId));
        model.addAttribute("form", new AprovacaoRelatorioFormDto());
        return "professor/concluirRelatorio";
    }

    @PostMapping("/{alunoOfertaId}/concluir-relatorio")
    public String concluirRelatorio(@PathVariable Long alunoOfertaId,
                                    @ModelAttribute("form") AprovacaoRelatorioFormDto form,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        try {
            professorService.concluirRelatorioResponsavel(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Relatório concluído com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/professor";
    }

    // PR.02 - Analisar Documentação (Responsável)
    @GetMapping("/{alunoOfertaId}/analisar-documentacao")
    public String exibirAnaliseDocumentacao(@PathVariable Long alunoOfertaId, Principal principal, Model model) {
        AlunoOferta alunoOferta = professorService.buscarAlunoOfertaComoResponsavel(alunoOfertaId, principal.getName());
        model.addAttribute("alunoOferta", alunoOferta);
        model.addAttribute("form", new AprovacaoRelatorioFormDto());
        return "professor/analisarDocumentacao";
    }

    @PostMapping("/{alunoOfertaId}/analisar-documentacao")
    public String analisarDocumentacao(@PathVariable Long alunoOfertaId,
                                       @ModelAttribute("form") AprovacaoRelatorioFormDto form,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes) {
        try {
            professorService.analisarDocumentacao(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Documentação analisada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/professor";
    }
}
