package br.ufscar.pescd.entity;

import br.ufscar.pescd.entity.enums.Perfil;
import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

}