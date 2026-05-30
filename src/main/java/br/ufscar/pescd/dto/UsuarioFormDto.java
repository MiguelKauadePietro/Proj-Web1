package br.ufscar.pescd.dto;

import br.ufscar.pescd.entity.Usuario;
import br.ufscar.pescd.entity.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioFormDto {

    @NotBlank
    private String nomeCompleto;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    private String senha;

    @NotNull
    private Perfil perfil;

    public static UsuarioFormDto de(Usuario usuario) {
        UsuarioFormDto dto = new UsuarioFormDto();
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setUsername(usuario.getUsername());
        dto.setPerfil(usuario.getPerfil());
        return dto;
    }
}
