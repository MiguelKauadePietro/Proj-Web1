package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.RelatorioEstagio;
import br.ufscar.pescd.entity.enums.Nota;
import br.ufscar.pescd.service.ProfessorService;
import br.ufscar.pescd.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor/ofertas")
@RequiredArgsConstructor
public class OfertaProfessorController {

    private final OfertaService ofertaService;
    private final ProfessorService professorService;

    @GetMapping("/{id}/encerrar")
    public String exibirFormularioEncerramento(
            @PathVariable Long id,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        Oferta oferta = ofertaService.buscarPorId(id);
        List<AlunoOferta> alunos = ofertaService.buscarAlunosPorOferta(id);
        Map<Long, RelatorioEstagio> relatorios = professorService.buscarRelatoriosDaOferta(id);

        if (!professorService.podeEncerrarOferta(id, principal.getName())) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Não é possível encerrar a oferta. Todos os alunos inscritos devem estar no status 'Concluído pelo responsável'.");
            return "redirect:/professor";
        }

        model.addAttribute("oferta", oferta);
        model.addAttribute("alunos", alunos);
        model.addAttribute("relatorios", relatorios);
        model.addAttribute("mediaFrequencia", calcularMediaFrequencia(alunos, relatorios));
        model.addAttribute("quantidadeEstagio", alunos.stream().filter(aluno -> aluno.getDocumentacao() == null).count());
        model.addAttribute("quantidadeDocumentacao", alunos.stream().filter(aluno -> aluno.getDocumentacao() != null).count());
        model.addAttribute("quantidadePorNota", calcularQuantidadePorNota(alunos, relatorios));

        return "professor/formEncerramento";
    }

    @PostMapping("/{id}/encerrar")
    public String solicitarEncerramento(
            @PathVariable Long id,
            @RequestParam String licoesAprendidas,
            @RequestParam String instrucaoEncerramento,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            professorService.encerrarOferta(
                    id,
                    principal.getName(),
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

    private Double calcularMediaFrequencia(List<AlunoOferta> alunos, Map<Long, RelatorioEstagio> relatorios) {
        return alunos.stream()
                .map(aluno -> obterFrequenciaFinal(aluno, relatorios))
                .filter(frequencia -> frequencia != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private Map<String, Long> calcularQuantidadePorNota(List<AlunoOferta> alunos, Map<Long, RelatorioEstagio> relatorios) {
        Map<String, Long> quantidadePorNota = new LinkedHashMap<>();
        for (Nota nota : Nota.values()) {
            long quantidade = alunos.stream()
                    .filter(aluno -> nota.name().equals(obterNotaFinal(aluno, relatorios)))
                    .count();
            quantidadePorNota.put(nota.name(), quantidade);
        }
        return quantidadePorNota;
    }

    private Double obterFrequenciaFinal(AlunoOferta alunoOferta, Map<Long, RelatorioEstagio> relatorios) {
        if (alunoOferta.getDocumentacao() != null) {
            return alunoOferta.getDocumentacao().getFrequencia();
        }

        RelatorioEstagio relatorio = relatorios.get(alunoOferta.getId());
        return relatorio != null ? relatorio.getFrequenciaResponsavel() : null;
    }

    private String obterNotaFinal(AlunoOferta alunoOferta, Map<Long, RelatorioEstagio> relatorios) {
        if (alunoOferta.getDocumentacao() != null && alunoOferta.getDocumentacao().getNota() != null) {
            return alunoOferta.getDocumentacao().getNota().name();
        }

        RelatorioEstagio relatorio = relatorios.get(alunoOferta.getId());
        return relatorio != null && relatorio.getNotaResponsavel() != null
                ? relatorio.getNotaResponsavel().name()
                : null;
    }
}
