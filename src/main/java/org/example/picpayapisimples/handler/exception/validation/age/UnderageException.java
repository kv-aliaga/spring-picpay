package org.example.picpayapisimples.handler.exception.validation.age;

public class UnderageException extends InvalidAgeException {
    public UnderageException() {
        super("Não é possível cadastrar pessoas com menos de 18 anos");
    }
}
