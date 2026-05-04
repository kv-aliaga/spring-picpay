package org.example.picpayapisimples.dto.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.picpayapisimples.utils.Formatador;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor @NoArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataNascimento;

    private Boolean ativo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataAtualizacao;

//    Getters formatados
    public String getCpf(){
        return Formatador.formatarCpf(cpf);
    }

    public String getTelefone(){
        return Formatador.formatarTelefone(telefone);
    }
}
