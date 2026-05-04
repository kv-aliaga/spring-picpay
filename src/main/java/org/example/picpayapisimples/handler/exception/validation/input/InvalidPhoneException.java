package org.example.picpayapisimples.handler.exception.validation.input;

// Lançada quando telefone inválido é digitado
public class InvalidPhoneException extends InvalidInputException {
    public InvalidPhoneException() {
        super("telefone");
    }
}
