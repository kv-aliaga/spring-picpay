package org.example.picpayapisimples.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.picpayapisimples.controller.base.BaseController;
import org.example.picpayapisimples.controller.base.BaseControllerCRUD;
import org.example.picpayapisimples.dto.conta.ContaRequestDTO;
import org.example.picpayapisimples.dto.conta.ContaResponseDTO;
import org.example.picpayapisimples.model.entity.Conta;
import org.example.picpayapisimples.service.ContaService;
import org.example.picpayapisimples.service.base.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contas")
public class ContaController
extends BaseController<ContaRequestDTO, ContaResponseDTO, Conta>
implements BaseControllerCRUD<ContaRequestDTO, ContaResponseDTO>
{
    private final ContaService service;

    public ContaController(BaseService<ContaRequestDTO, ContaResponseDTO, Conta> service, ContaService service1) {
        super(service);
        this.service = service1;
    }


    @Override
    public ResponseEntity<ContaResponseDTO> patch(Long id, Map<String, Object> campos) {
        return ResponseEntity.ok(service.patch(id, campos));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable @Positive Long id){
        service.inativarConta(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/saldo")
    public ResponseEntity<ContaResponseDTO> alterarSaldo(@PathVariable @Positive Long id, @RequestParam @NotNull BigDecimal valor){
        return ResponseEntity.ok(service.alterarSaldo(id, valor));
    }

    @PatchMapping("/{id}/tipo")
    public ResponseEntity<ContaResponseDTO> alterarTipoConta(@PathVariable @Positive Long id, @RequestParam(value = "conta") String tipoString){
        return ResponseEntity.ok(service.alterarTipoConta(id, tipoString));
    }

    @PatchMapping("/transferencia")
    public ResponseEntity<List<ContaResponseDTO>> transferencia(
            @RequestParam(value = "origem") @Positive Long idOrigem,
            @RequestParam(value = "destino") @Positive Long idDestino,
            @RequestParam(value = "valor") BigDecimal valor
        ){
        return ResponseEntity.ok(service.transferencia(idOrigem, idDestino, valor));
    }
}
