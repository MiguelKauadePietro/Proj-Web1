package br.ufscar.pescd.entity.enums;

public enum Perfil {
    ADMINISTRADOR,
    SECRETARIO,
    PROFESSOR,
    ALUNO;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
