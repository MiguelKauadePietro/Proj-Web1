package br.ufscar.pescd.exception;

public class DataFimAnteriorAoInicioException extends PescdException {

    public DataFimAnteriorAoInicioException() {
        super("A data de fim deve ser posterior à data de início.");
    }
}
