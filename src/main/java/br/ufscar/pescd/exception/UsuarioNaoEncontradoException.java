package br.ufscar.pescd.exception;

public class UsuarioNaoEncontradoException extends PescdException {

    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }
}
