package org.example.picpayapisimples.controller;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.example.picpayapisimples.controller.base.BaseController;
import org.example.picpayapisimples.controller.base.BaseControllerCRUD;
import org.example.picpayapisimples.dto.cliente.ClienteRequestDTO;
import org.example.picpayapisimples.dto.cliente.ClienteResponseDTO;
import org.example.picpayapisimples.model.entity.Cliente;
import org.example.picpayapisimples.service.ClienteService;
import org.example.picpayapisimples.service.base.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/clientes")
@Getter
public class ClienteController
extends BaseController<ClienteRequestDTO, ClienteResponseDTO, Cliente>
implements BaseControllerCRUD<ClienteRequestDTO, ClienteResponseDTO>
{
    private final ClienteService service;

    public ClienteController(BaseService<ClienteRequestDTO, ClienteResponseDTO, Cliente> service, ClienteService service1) {
        super(service);
        this.service = service1;
    }


    @Override
    public ResponseEntity<ClienteResponseDTO> patch(Long id, Map<String, Object> campos) {
        return ResponseEntity.ok(service.patch(id, campos));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable @Positive Long id){
        service.inativarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
