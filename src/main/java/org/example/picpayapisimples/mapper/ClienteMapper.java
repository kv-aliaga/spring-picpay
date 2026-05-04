package org.example.picpayapisimples.mapper;

import org.example.picpayapisimples.dto.cliente.ClienteRequestDTO;
import org.example.picpayapisimples.dto.cliente.ClienteResponseDTO;
import org.example.picpayapisimples.model.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper implements BaseMapper<ClienteRequestDTO, ClienteResponseDTO, Cliente>{
    @Override
    public ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDataNascimento(),
                cliente.getAtivo(),
                cliente.getDataCriacao(),
                cliente.getDataAtualizacao()
        );
    }

    @Override
    public Cliente toModel(ClienteRequestDTO dto) {
        return new Cliente(
                dto.getCpf(),
                dto.getNome(),
                dto.getEmail(),
                dto.getTelefone(),
                dto.getDataNascimento()
        );
    }
}
