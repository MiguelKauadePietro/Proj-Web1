package br.ufscar.pescd.exception;

public class MatriculaNaoEncontradaException extends PescdException {

    public MatriculaNaoEncontradaException() {
        super("Registro de matrícula não encontrado.");
    }
}
