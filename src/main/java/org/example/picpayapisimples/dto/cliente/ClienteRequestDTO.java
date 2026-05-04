package org.example.picpayapisimples.dto.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ClienteRequestDTO {
    @CPF
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF precisa ter 11 caracteres")
    private String cpf;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 60, message = "Nome não pode ter mais de 60 caracteres")
    private String nome;

    @Email(message = "Digite um e-mail válido")
    @NotBlank(message = "E-mail é obrigatório")
    @Size(max = 255, message = "E-mail não pode ter mais de 255 caracteres")
    private String email;

    @Pattern(regexp = "^(55)?([1-9]{2})?([0-9]{4,5})([0-9]{4})$", message = "Digite um telefone válido")
    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "Telefone não pode ter mais de 20 caracteres")
    private String telefone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "Data de nascimento precisa estar no passado")
    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate dataNascimento;
}
