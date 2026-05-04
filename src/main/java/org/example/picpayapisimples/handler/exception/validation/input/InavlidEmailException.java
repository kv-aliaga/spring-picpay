package org.example.picpayapisimples.handler.exception.validation.input;

// Lançada quando e-mail inválido é digitado
public class InavlidEmailException extends InvalidInputException {
    public InavlidEmailException() {
        super("e-mail");
    }
}
