package org.example.picpayapisimples.handler.exception.validation.input;

// Lançada quando tipo da conta inválido é digitado
public class InvalidAccountTypeException extends InvalidInputException {
    public InvalidAccountTypeException() {
        super("tipo da conta");
    }
}
