package org.example.picpayapisimples.handler.exception.validation.input;

import org.example.picpayapisimples.handler.exception.validation.ValidationException;

// Lançada quando input é inválido
public class InvalidInputException extends ValidationException {
    public InvalidInputException(String campo) {
        super("Campo " + campo + " digitado não é valido");
    }
}
