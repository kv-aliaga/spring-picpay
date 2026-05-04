package org.example.picpayapisimples.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.picpayapisimples.model.BaseModel;
import org.example.picpayapisimples.model.enums.TipoConta;

import java.math.BigDecimal;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "tb_conta")
public class Conta extends BaseModel {
    @Column(name = "numero", unique = true, updatable = false, length = 5, nullable = false)
    private String numero;

    @Column(name = "agencia", length = 60, nullable = false)
    private String agencia;

    @Column(name = "saldo", precision = 15, secondPrecision = 2, nullable = false)
    private BigDecimal saldo;

    @Column(name = "tipo_conta", nullable = false, length = 13)
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente", nullable = false, updatable = false)
    private Cliente cliente;
}
