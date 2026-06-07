package br.ufscar.pescd.config;

import br.ufscar.pescd.entity.enums.Perfil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthSuccessHandler implements
        AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        String redirect = "/";
        for (Perfil perfil : Perfil.values()) {
            if (perfil.getRole().equals(role)) {
                redirect = switch (perfil) {
                    case ADMINISTRADOR -> "/admin/usuarios";
                    case SECRETARIO -> "/secretario/ofertas";
                    case ALUNO -> "/aluno/ofertas";
                    case PROFESSOR -> "/professor";
                };
                break;
            }
        }

        response.sendRedirect(request.getContextPath() + redirect);
    }
}
