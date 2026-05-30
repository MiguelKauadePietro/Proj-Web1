package br.ufscar.pescd.exception;

public class UsernameJaCadastradoException extends PescdException {

    public UsernameJaCadastradoException() {
        super("Nome de usuário já cadastrado.");
    }
}
