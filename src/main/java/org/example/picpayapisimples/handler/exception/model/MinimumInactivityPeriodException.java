package org.example.picpayapisimples.handler.exception.model;

// Lançada na tentativa de excluir models inativos que ainda não passaram do tempo máximo de permanência (6 meses)
public class MinimumInactivityPeriodException extends ModelException {
    public MinimumInactivityPeriodException(Integer qtdMeses) {
        super("Não é possível excluir dados com menos de seis meses de inatividade (" + qtdMeses + " meses inativo)");
    }
}
