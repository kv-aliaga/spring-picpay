package org.example.picpayapisimples.handler.exception.patch;

// Lançada na tentativa de patch de campo imutável
public class ImmutableFieldException extends PatchException {
    public ImmutableFieldException(String campo) {
        super("Campo " + campo.toLowerCase() + " não pode ser alterado");
    }
}
