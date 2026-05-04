package org.example.picpayapisimples.handler.exception.model;

import org.example.picpayapisimples.handler.exception.BusinessException;

public class ModelException extends BusinessException {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(){
        super("Houve um erro inesperado na tentativa de realizar a ação");
    }
}
