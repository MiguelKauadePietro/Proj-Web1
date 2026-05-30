package br.ufscar.pescd.exception;

public class DesativarProprioUsuarioException extends PescdException {

    public DesativarProprioUsuarioException() {
        super("Não é possível desativar o próprio usuário.");
    }
}
