package org.example.picpayapisimples.handler.exception.transaction;

import org.example.picpayapisimples.handler.exception.BusinessException;

// Todas as exceções de transação
public class TransactionException extends BusinessException {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(){
        super("Houve um erro inesperado na tentativa de transação");
    }
}
