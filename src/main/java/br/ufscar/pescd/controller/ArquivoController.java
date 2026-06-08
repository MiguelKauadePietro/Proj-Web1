package br.ufscar.pescd.controller;

import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.repository.UsuarioRepositorio;
import br.ufscar.pescd.service.AlunoOfertaService;
import br.ufscar.pescd.service.ArquivoStorageService;
import br.ufscar.pescd.service.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import java.nio.file.Path;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final UsuarioRepositorio usuarioRepositorio;
    private final AlunoOfertaService alunoOfertaService;
    private final ProfessorService professorService;
    private final ArquivoStorageService arquivoStorageService;

    @GetMapping("/{tipo}/{alunoOfertaId}")
    public ResponseEntity<Resource> visualizarArquivo(
            @PathVariable String tipo,
            @PathVariable Long alunoOfertaId,
            Principal principal) {
        Usuario usuario = usuarioRepositorio.findByUsernameAndAtivoTrue(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        String arquivoPath = switch (usuario.getPerfil()) {
            case ALUNO -> alunoOfertaService.buscarArquivoDoAluno(alunoOfertaId, usuario.getUsername(), tipo);
            case PROFESSOR -> professorService.buscarArquivoDoProfessor(alunoOfertaId, usuario.getUsername(), tipo);
            default -> throw new AccessDeniedException("Você não tem permissão para acessar este arquivo.");
        };

        Path arquivo = arquivoStorageService.carregarArquivo(arquivoPath);
        Resource resource = new FileSystemResource(arquivo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(arquivo.getFileName().toString()).build().toString())
                .body(resource);
    }
}
