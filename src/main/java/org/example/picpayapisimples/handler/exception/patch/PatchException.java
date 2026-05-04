package org.example.picpayapisimples.handler.exception.patch;

import org.example.picpayapisimples.handler.exception.BusinessException;

public class PatchException extends BusinessException {
    public PatchException(String message) {
        super(message);
    }

    public PatchException(){
        super("Houve um erro inesperado na tentativa da atualização");
    }
}
