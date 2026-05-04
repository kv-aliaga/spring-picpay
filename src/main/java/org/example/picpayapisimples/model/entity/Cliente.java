package org.example.picpayapisimples.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.picpayapisimples.model.BaseModel;

import java.time.LocalDate;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "tb_cliente")
public class Cliente extends BaseModel {
    @Column(name = "cpf", nullable = false, unique = true, updatable = false)
    private String cpf;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;

    @Column(name = "data_nascimento", nullable = false, updatable = false)
    private LocalDate dataNascimento;
}
