package br.ufscar.pescd.exception;

public class EmailJaCadastradoException extends PescdException {

    public EmailJaCadastradoException() {
        super("E-mail já cadastrado.");
    }
}
