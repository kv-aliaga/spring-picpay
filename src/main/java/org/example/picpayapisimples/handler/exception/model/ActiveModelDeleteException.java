package org.example.picpayapisimples.handler.exception.model;

// Lançado ao tentar excluir models ativos
public class ActiveModelDeleteException extends ModelException {
    public ActiveModelDeleteException(String className) {
        super("Não é possível fazer a exclusão de " + className.toLowerCase() + " se for ativo");
    }
}
