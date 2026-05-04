package org.example.picpayapisimples.handler.exception.transaction;

// Lançada na tentativa de transferência de valor menor ou igual a zero
public class NegativeOrZeroTransferException extends TransactionException {
    public NegativeOrZeroTransferException() {
        super("A transferência não pode ser de um valor menor que zero");
    }
}
