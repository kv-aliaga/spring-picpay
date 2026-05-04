package org.example.picpayapisimples.handler.exception.patch;

// Lançada na tentativa de patch de campo inexistente
public class InexistentFieldPatchException extends PatchException{
    public InexistentFieldPatchException(String campo, String className) {
        super("Campo " + campo + " não existe em " + className.toLowerCase());
    }
}
