package org.example.picpayapisimples.dto.conta;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ContaRequestDTO {
    @NotBlank(message = "Número da agência é obrigatório")
    @Pattern(regexp = "\\d{5}", message = "Número da agência precisa ter 5 caracteres")
    private String numero;

    @NotBlank(message = "Agência é obrigatório")
    @Size(max = 60, message = "Agência não pode ter mais de 60 caracteres")
    private String agencia;

    @NotNull(message = "Saldo é obrigatório")
    @DecimalMin(value = "-200.00", message = "Saldo mínimo permitido é R$-200,00")
    @Digits(integer = 15, fraction = 2, message = "Saldo máximo atingido")
    private BigDecimal saldo;

    @NotNull(message = "Tipo da conta é obrigatório")
    private String tipo;

    @NotNull(message = "ID do cliente é obrigatório")
    @Positive(message = "ID do cliente precisa ser maior que zero")
    private Long idCliente;

}
