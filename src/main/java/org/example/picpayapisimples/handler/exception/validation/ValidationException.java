package org.example.picpayapisimples.handler.exception.validation;

import org.example.picpayapisimples.handler.exception.BusinessException;

// Todas as exceções de validação
public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(){
        super("Houve um erro no cadastro de dados");
    }
}
