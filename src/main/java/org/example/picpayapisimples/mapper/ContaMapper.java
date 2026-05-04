package org.example.picpayapisimples.mapper;

import org.apache.commons.lang3.*;
import org.example.picpayapisimples.dto.conta.*;
import org.example.picpayapisimples.model.entity.Conta;
import org.example.picpayapisimples.model.enums.TipoConta;
import org.example.picpayapisimples.utils.Formatador;
import org.springframework.stereotype.Component;

@Component
public class ContaMapper implements BaseMapper<ContaRequestDTO, ContaResponseDTO, Conta> {
    @Override
    public Conta toModel(ContaRequestDTO dto) {
        Conta conta = new Conta();
        TipoConta tipoConta = EnumUtils.getEnumIgnoreCase(TipoConta.class, StringUtils.stripAccents(dto.getTipo()));

        conta.setNumero(dto.getNumero());
        conta.setAgencia(dto.getAgencia());
        conta.setSaldo(dto.getSaldo());
        conta.setTipo(tipoConta);

        return conta;
    }

    @Override
    public ContaResponseDTO toResponse(Conta conta) {
        return new ContaResponseDTO(
                conta.getId(),
                conta.getNumero(),
                conta.getAgencia(),
                Formatador.moeda.format(conta.getSaldo()),
                conta.getTipo(),
                conta.getCliente().getNome(),
                conta.getAtivo(),
                conta.getDataCriacao(),
                conta.getDataAtualizacao()
        );
    }
}
