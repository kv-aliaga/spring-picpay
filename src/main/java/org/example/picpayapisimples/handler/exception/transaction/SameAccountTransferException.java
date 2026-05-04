package org.example.picpayapisimples.handler.exception.transaction;

// Lançada quando uma conta quer transferir para ela mesma
public class SameAccountTransferException extends TransactionException {
    public SameAccountTransferException() {
        super("Não é possível fazer uma transação para sua própria conta");
    }
}
