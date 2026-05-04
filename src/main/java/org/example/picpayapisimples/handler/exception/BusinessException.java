package org.example.picpayapisimples.handler.exception;

// Superclasse de todas as exceções de regra de negócio
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(){
        super("Houve um erro inesperado");
    }
}
