package org.example.picpayapisimples.controller.base;

import lombok.AllArgsConstructor;
import org.example.picpayapisimples.model.BaseModel;
import org.example.picpayapisimples.service.base.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor
public abstract class BaseController<REQ, RESP, MOD extends BaseModel>
implements BaseControllerCRD<REQ, RESP>{
    private final BaseService<REQ, RESP, MOD> service;

    @Override
    public ResponseEntity<RESP> inserir(REQ dto) {
        RESP salvo = service.inserir(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @Override
    public ResponseEntity<Void> excluir(Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RESP> buscarPorId(Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    public ResponseEntity<List<RESP>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }
}
