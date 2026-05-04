package org.example.picpayapisimples.handler.exception.transaction;

// Lançada na tentativa de atualização de saldo de zero reais
public class ZeroAmountException extends TransactionException {
    public ZeroAmountException() {
        super("Não é possível fazer uma transação de R$0,00");
    }
}
