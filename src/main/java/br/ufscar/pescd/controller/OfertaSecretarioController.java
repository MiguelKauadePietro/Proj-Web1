package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.AlunoOferta;
import br.ufscar.pescd.entity.Oferta;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import br.ufscar.pescd.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/secretario/ofertas")
@RequiredArgsConstructor
public class OfertaSecretarioController {

    private final OfertaService ofertaService;
    private final UsuarioRepositorio usuarioRepositorio;


    // MÉTODOS DA USER STORY S.01 (Criar Oferta)

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

    // MÉTODOS DA USER STORY S.02 (Gerenciar Alunos)

    @GetMapping("/{id}/alunos")
    public String gerenciarAlunos(@PathVariable Long id, Model model) {
        Oferta oferta = ofertaService.buscarPorId(id);
        List<AlunoOferta> alunosDaOferta = ofertaService.buscarAlunosPorOferta(id);

        model.addAttribute("oferta", oferta);
        model.addAttribute("alunos", alunosDaOferta);
        return "secretario/alunos";
    }

    // 2. CRUD: Adicionar aluno manualmente por username/e-mail
    @PostMapping("/{id}/alunos/adicionar")
    public String adicionarAlunoManual(@PathVariable Long id, @RequestParam String username, RedirectAttributes redirectAttributes) {
        try {
            Usuario aluno = usuarioRepositorio.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            ofertaService.matricularAlunoNaOferta(id, aluno);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aluno adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao adicionar aluno: " + e.getMessage());
        }
        return "redirect:/secretario/ofertas/" + id + "/alunos";
    }

    // 3. Upload de CSV: Processa o arquivo enviado
    @PostMapping("/{id}/alunos/upload-csv")
    public String uploadCSV(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo, RedirectAttributes redirectAttributes) {
        if (arquivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Por favor, selecione um arquivo CSV.");
            return "redirect:/secretario/ofertas/" + id + "/alunos";
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(arquivo.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            int contagem = 0;

            boolean primeiraLinha = true;
            while ((linha = fileReader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    if(linha.contains("@") || !linha.equalsIgnoreCase("username")) {
                        // Se não for cabeçalho, processa direto
                    } else {
                        continue;
                    }
                }

                String username = linha.trim().replace(";", "");
                if (!username.isEmpty()) {
                    Usuario aluno = usuarioRepositorio.findByUsername(username).orElse(null);
                    if (aluno != null) {
                        ofertaService.matricularAlunoNaOferta(id, aluno);
                        contagem++;
                    }
                }
            }
            redirectAttributes.addFlashAttribute("mensagemSucesso", contagem + " alunos importados com sucesso via CSV!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao processar o arquivo CSV: " + e.getMessage());
        }

        return "redirect:/secretario/ofertas/" + id + "/alunos";
    }

    // 4. CRUD: Remover aluno da oferta
    @GetMapping("/{ofertaId}/alunos/remover/{alunoOfertaId}")
    public String removerAluno(@PathVariable Long ofertaId, @PathVariable Long alunoOfertaId, RedirectAttributes redirectAttributes) {
        try {
            ofertaService.removerAlunoDaOferta(alunoOfertaId);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aluno removido da oferta.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao remover aluno.");
        }
        return "redirect:/secretario/ofertas/" + ofertaId + "/alunos";
    }
}