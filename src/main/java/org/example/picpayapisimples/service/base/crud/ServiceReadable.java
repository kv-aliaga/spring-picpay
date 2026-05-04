package org.example.picpayapisimples.service.base.crud;

import java.util.List;

public interface ServiceReadable<RESP> {
    RESP buscarPorId(Long id);
    List<RESP> buscarTodos();
}
