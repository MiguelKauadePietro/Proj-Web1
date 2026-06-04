package br.ufscar.pescd.controller;

import br.ufscar.pescd.dto.DocumentacaoDocenciaFormDto;
import br.ufscar.pescd.dto.PlanoTrabalhoFormDto;
import br.ufscar.pescd.dto.RelatorioFinalFormDto;
import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.LogStatusAluno;
import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.entity.Usuario;
import jakarta.validation.Valid;
import br.ufscar.pescd.service.AlunoOfertaService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/aluno/ofertas")
@RequiredArgsConstructor
public class AlunoOfertaController {

    private final AlunoOfertaService alunoOfertaService;

    @GetMapping
    public String listar(Principal principal, Model model) {
        List<AlunoOferta> ofertas = alunoOfertaService.listarDoAluno(principal.getName());
        model.addAttribute("ofertasAluno", ofertas);
        return "aluno/ofertas/lista";
    }

    @GetMapping("/{alunoOfertaId}")
    public String detalhar(@PathVariable Long alunoOfertaId, Principal principal, Model model) {
        AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());
        List<LogStatusAluno> historico = alunoOfertaService.listarHistorico(alunoOfertaId, principal.getName());

        model.addAttribute("alunoOferta", alunoOferta);
        model.addAttribute("historicoStatus", historico);
        return "aluno/ofertas/detalhe";
    }

    @GetMapping("/{alunoOfertaId}/plano")
    public String exibirFormularioPlano(
            @PathVariable Long alunoOfertaId,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            alunoOfertaService.validarDisponibilidadeEnvioPlano(alunoOfertaId, principal.getName());
            AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());
            prepararFormularioPlano(model, alunoOferta, new PlanoTrabalhoFormDto());
            return "aluno/ofertas/plano-form";
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
            return "redirect:/aluno/ofertas/" + alunoOfertaId;
        }
    }

    @PostMapping("/{alunoOfertaId}/plano")
    public String enviarPlano(
            @PathVariable Long alunoOfertaId,
            @ModelAttribute("planoForm") @Valid PlanoTrabalhoFormDto form,
            BindingResult bindingResult,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());

        if (form.getArquivo() == null || form.getArquivo().isEmpty()) {
            bindingResult.rejectValue("arquivo", "student.plan.file.required");
        }

        if (bindingResult.hasErrors()) {
            prepararFormularioPlano(model, alunoOferta, form);
            return "aluno/ofertas/plano-form";
        }

        try {
            alunoOfertaService.enviarPlano(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Plano de trabalho enviado com sucesso.");
            return "redirect:/aluno/ofertas/" + alunoOfertaId;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            prepararFormularioPlano(model, alunoOferta, form);
            return "aluno/ofertas/plano-form";
        }
    }

    @GetMapping("/{alunoOfertaId}/documentacao")
    public String exibirFormularioDocumentacao(
            @PathVariable Long alunoOfertaId,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            alunoOfertaService.validarDisponibilidadeEnvioDocumentacao(alunoOfertaId, principal.getName());
            AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());
            model.addAttribute("alunoOferta", alunoOferta);
            model.addAttribute("documentacaoForm", new DocumentacaoDocenciaFormDto());
            return "aluno/ofertas/documentacao-form";
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
            return "redirect:/aluno/ofertas/" + alunoOfertaId;
        }
    }

    @PostMapping("/{alunoOfertaId}/documentacao")
    public String enviarDocumentacao(
            @PathVariable Long alunoOfertaId,
            @ModelAttribute("documentacaoForm") @Valid DocumentacaoDocenciaFormDto form,
            BindingResult bindingResult,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());

        if (form.getArquivo() == null || form.getArquivo().isEmpty()) {
            bindingResult.rejectValue("arquivo", "student.documentation.file.required");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("alunoOferta", alunoOferta);
            return "aluno/ofertas/documentacao-form";
        }

        try {
            alunoOfertaService.enviarDocumentacao(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Documentação enviada com sucesso.");
            return "redirect:/aluno/ofertas/" + alunoOfertaId;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("alunoOferta", alunoOferta);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "aluno/ofertas/documentacao-form";
        }
    }

    @GetMapping("/{alunoOfertaId}/relatorio")
    public String exibirFormularioRelatorio(
            @PathVariable Long alunoOfertaId,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            alunoOfertaService.validarDisponibilidadeEnvioRelatorio(alunoOfertaId, principal.getName());
            AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());
            List<LogStatusAluno> historico = alunoOfertaService.listarHistorico(alunoOfertaId, principal.getName());
            RelatorioEstagio relatorioEstagio = alunoOfertaService.buscarRelatorio(alunoOfertaId, principal.getName());

            model.addAttribute("alunoOferta", alunoOferta);
            model.addAttribute("historicoStatus", historico);
            model.addAttribute("relatorioExistente", relatorioEstagio);
            model.addAttribute("relatorioForm", new RelatorioFinalFormDto());
            return "aluno/ofertas/relatorio-form";
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
            return "redirect:/aluno/ofertas/" + alunoOfertaId;
        }
    }

    @PostMapping("/{alunoOfertaId}/relatorio")
    public String enviarRelatorio(
            @PathVariable Long alunoOfertaId,
            @ModelAttribute("relatorioForm") @Valid RelatorioFinalFormDto form,
            BindingResult bindingResult,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        AlunoOferta alunoOferta = alunoOfertaService.buscarDoAluno(alunoOfertaId, principal.getName());
        List<LogStatusAluno> historico = alunoOfertaService.listarHistorico(alunoOfertaId, principal.getName());
        RelatorioEstagio relatorioEstagio = alunoOfertaService.buscarRelatorio(alunoOfertaId, principal.getName());

        if (form.getArquivo() == null || form.getArquivo().isEmpty()) {
            bindingResult.rejectValue("arquivo", "student.report.file.required");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("alunoOferta", alunoOferta);
            model.addAttribute("historicoStatus", historico);
            model.addAttribute("relatorioExistente", relatorioEstagio);
            return "aluno/ofertas/relatorio-form";
        }

        try {
            alunoOfertaService.enviarRelatorioFinal(alunoOfertaId, principal.getName(), form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Relatório final enviado com sucesso.");
            return "redirect:/aluno/ofertas/" + alunoOfertaId;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("alunoOferta", alunoOferta);
            model.addAttribute("historicoStatus", historico);
            model.addAttribute("relatorioExistente", relatorioEstagio);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "aluno/ofertas/relatorio-form";
        }
    }

    private void prepararFormularioPlano(Model model, AlunoOferta alunoOferta, PlanoTrabalhoFormDto form) {
        List<Usuario> professores = alunoOfertaService.listarProfessores();
        model.addAttribute("alunoOferta", alunoOferta);
        model.addAttribute("professores", professores);
        model.addAttribute("planoForm", form);
    }
}
