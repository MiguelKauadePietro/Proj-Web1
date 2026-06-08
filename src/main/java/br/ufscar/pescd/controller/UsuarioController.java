package br.ufscar.pescd.controller;

import br.ufscar.pescd.dto.UsuarioFormDto;
import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import br.ufscar.pescd.exception.PescdException;
import br.ufscar.pescd.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // AD.01
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarGerenciaveis());
        return "admin/usuarios/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("usuarioForm", new UsuarioFormDto());
        model.addAttribute("perfis", List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR));
        return "admin/usuarios/form";
    }

    @PostMapping("/novo")
    public String criar(@Valid @ModelAttribute("usuarioForm") UsuarioFormDto dto,
                        BindingResult result, Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("perfis", List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR));
            return "admin/usuarios/form";
        }
        try {
            usuarioService.criar(dto);
            redirectAttrs.addFlashAttribute("sucesso", "Usuário criado com sucesso.");
        } catch (PescdException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("perfis", List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR));
            return "admin/usuarios/form";
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuarioForm", UsuarioFormDto.toDto(usuario));
        model.addAttribute("usuarioId", id);
        model.addAttribute("editandoProprioUsuario", usuario.getUsername().equals(userDetails.getUsername()));
        model.addAttribute("perfis", List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR));
        return "admin/usuarios/form";
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         @Valid @ModelAttribute("usuarioForm") UsuarioFormDto dto,
                         BindingResult result, Model model,
                         @AuthenticationPrincipal UserDetails userDetails,
                         RedirectAttributes redirectAttrs) {
        boolean editandoProprioUsuario = userDetails.getUsername().equals(dto.getUsername());
        if (result.hasErrors()) {
            model.addAttribute("usuarioId", id);
            model.addAttribute("editandoProprioUsuario", editandoProprioUsuario);
            model.addAttribute("perfis", List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR));
            return "admin/usuarios/form";
        }
        try {
            usuarioService.editar(id, dto, userDetails.getUsername());
            redirectAttrs.addFlashAttribute("sucesso", "Usuário atualizado com sucesso.");
        } catch (PescdException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuarioId", id);
            model.addAttribute("editandoProprioUsuario", editandoProprioUsuario);
            model.addAttribute("perfis", List.of(Perfil.ADMINISTRADOR, Perfil.SECRETARIO, Perfil.PROFESSOR));
            return "admin/usuarios/form";
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/desativar")
    public String desativar(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttrs) {
        try {
            usuarioService.desativar(id, userDetails.getUsername());
            redirectAttrs.addFlashAttribute("sucesso", "Usuário desativado com sucesso.");
        } catch (PescdException e) {
            redirectAttrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
