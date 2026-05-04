package org.example.picpayapisimples.handler.exception.transaction;

// Lançado na tentativa de transação ultrapassar o limite de R$-200,00
public class InsufficientBalanceException extends TransactionException {
    public InsufficientBalanceException() {
        super("O limite de R$200,00 foi atingido e não foi possível terminar a transação");
    }
}
