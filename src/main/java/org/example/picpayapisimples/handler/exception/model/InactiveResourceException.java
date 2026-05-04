package org.example.picpayapisimples.handler.exception.model;

import org.apache.commons.lang3.StringUtils;

// Lançado na tentativa de realizar alguma ação em model inativo
public class InactiveResourceException extends ModelException {
    public InactiveResourceException(String className) {
        super(StringUtils.capitalize(className) + " não pode sofrer a ação já que está inativado");
    }
}
