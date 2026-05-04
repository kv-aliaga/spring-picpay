package org.example.picpayapisimples.handler.exception.validation.age;

import org.example.picpayapisimples.handler.exception.validation.ValidationException;

// Lançada na tentativa de cadastro de clientes menores de 18 anos
public class InvalidAgeException extends ValidationException {
    public InvalidAgeException() {
        super("Não é possível cadastrar pessoas com a idade digitada");
    }

    public InvalidAgeException(String mensagem){
        super(mensagem);
    }
}
