package br.ufscar.pescd.exception;

public class AlterarProprioPerfilException extends PescdException {

    public AlterarProprioPerfilException() {
        super("Não é possível alterar o perfil do próprio usuário.");
    }
}
