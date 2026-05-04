package org.example.picpayapisimples.handler.exception.validation.input;

// Lançada quando DDD inválido é digitado
public class InvalidDDDException extends InvalidInputException {
    public InvalidDDDException() {
        super("DDD");
    }
}
