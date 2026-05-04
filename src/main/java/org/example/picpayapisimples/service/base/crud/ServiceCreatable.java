package org.example.picpayapisimples.service.base.crud;

public interface ServiceCreatable<REQ, RESP> {
    RESP inserir(REQ dto);
}
