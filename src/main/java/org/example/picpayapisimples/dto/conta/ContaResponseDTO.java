package org.example.picpayapisimples.dto.conta;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.picpayapisimples.model.enums.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class ContaResponseDTO {
    private Long id;
    private String numero;
    private String agencia;
    private String saldo;
    private TipoConta tipoConta;
    private String nomeCliente;
    private Boolean ativo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataAtualizacao;
}
