package org.example.picpayapisimples.handler.exception.validation.input;

// Lançada quando CPF inválido é digitado
public class InvalidCPFException extends InvalidInputException {
    public InvalidCPFException() {
        super("CPF");
    }
}
