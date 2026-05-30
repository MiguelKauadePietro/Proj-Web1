package br.ufscar.pescd.config;

import br.ufscar.pescd.repository.UsuarioRepositorio;
import br.ufscar.pescd.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final UsuarioRepositorio usuarioRepositorio;

    public WebSecurityConfiguration(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(usuarioRepositorio);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/ofertas", "/login/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.png").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/secretario/**").hasRole("SECRETARIO")
                .requestMatchers("/aluno/**").hasRole("ALUNO")
                .requestMatchers("/professor/**").hasRole("PROFESSOR")
                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(form -> form
                .loginPage("/login")
                .passwordParameter("senha")
                .successHandler(successHandler)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
