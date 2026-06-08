package br.ufscar.pescd.exception;

public class AlunoJaMatriculadoException extends PescdException {

    public AlunoJaMatriculadoException() {
        super("Este aluno já está matriculado nesta oferta.");
    }
}
