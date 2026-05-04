package org.example.picpayapisimples.handler.exception.patch;

import org.apache.commons.lang3.StringUtils;

// Lançada quando UNIQUE é violado
public class UniqueViolationException extends PatchException {
    public UniqueViolationException(String campo, String valor) {
        super(StringUtils.capitalize(campo) + " com o valor " + valor + " já foi cadastrado.");
    }
}
