package br.ufscar.pescd.exception;

public class OfertaConcluidaException extends PescdException {

    public OfertaConcluidaException() {
        super("Operação não permitida: a oferta está concluída.");
    }
}
