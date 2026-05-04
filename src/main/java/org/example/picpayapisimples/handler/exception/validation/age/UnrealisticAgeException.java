package org.example.picpayapisimples.handler.exception.validation.age;

public class UnrealisticAgeException extends InvalidAgeException{
    public UnrealisticAgeException() {
        super("Não é possível cadastrar pessoas com mais de 120 anos");
    }
}
