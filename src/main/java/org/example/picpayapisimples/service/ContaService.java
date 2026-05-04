package org.example.picpayapisimples.service;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.picpayapisimples.dto.conta.ContaRequestDTO;
import org.example.picpayapisimples.dto.conta.ContaResponseDTO;
import org.example.picpayapisimples.handler.exception.patch.PatchException;
import org.example.picpayapisimples.handler.exception.transaction.InsufficientBalanceException;
import org.example.picpayapisimples.handler.exception.transaction.NegativeOrZeroTransferException;
import org.example.picpayapisimples.handler.exception.transaction.SameAccountTransferException;
import org.example.picpayapisimples.handler.exception.validation.input.InvalidAccountTypeException;
import org.example.picpayapisimples.mapper.ContaMapper;
import org.example.picpayapisimples.model.entity.Cliente;
import org.example.picpayapisimples.model.entity.Conta;
import org.example.picpayapisimples.model.enums.TipoConta;
import org.example.picpayapisimples.repository.ContaRepository;
import org.example.picpayapisimples.service.base.BaseServiceCRUD;
import org.example.picpayapisimples.service.base.BaseService;
import org.example.picpayapisimples.utils.PatchConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ContaService
extends BaseService<ContaRequestDTO, ContaResponseDTO, Conta>
implements BaseServiceCRUD<ContaRequestDTO, ContaResponseDTO> {
    private final ContaRepository repo;
    private final ClienteService clienteService;
    private static final PatchConfig patchConfig = new PatchConfig(
            Set.of("numero", "agencia", "saldo", "tipo", "idCliente"),
            Set.of("agencia", "saldo", "tipo")
    );
    private static final BigDecimal limiteNegativo = new BigDecimal("-200");

    public ContaService(ContaRepository repo, ContaMapper mapper, ClienteService clienteService){
        super("Conta", repo, mapper);
        this.repo = repo;
        this.clienteService = clienteService;
    }

    @Override
    protected void antesInserir(ContaRequestDTO dto, Conta conta) {
        Cliente cliente = clienteService.getModel(dto.getIdCliente());

        checkInativo(cliente.getAtivo());
        conta.setCliente(cliente);
    }

    @Override
    public ContaResponseDTO inserir(ContaRequestDTO dto) {
        checkTipoConta(dto.getTipo());

        return super.inserir(dto);
    }

    @Override
    public ContaResponseDTO patch(Long id, Map<String, Object> campos) {
        Conta conta = getModel(id);
        checkInativo(conta.getAtivo());
        checkCamposPatch(patchConfig, campos);

        if (campos.containsKey("agencia")) conta.setAgencia(campos.get("agencia").toString());

        Conta salvo = repo.save(conta);
        return mapper.toResponse(salvo);
    }

//    Métodos
    @Transactional
    public void inativarConta(Long id){
        Conta conta = getModel(id);
        if (!conta.getAtivo()) throw new PatchException("A conta já está inativa");

        repo.inativarConta(conta.getId());
    }

    @Transactional
    public ContaResponseDTO alterarSaldo(Long id, BigDecimal saldoAlterar){
        Conta conta = getModel(id);
        checkInativo(conta.getAtivo());
        BigDecimal saldoFinal = conta.getSaldo().add(saldoAlterar);

        if (saldoFinal.compareTo(limiteNegativo) < 0) throw new InsufficientBalanceException();
        repo.alterarSaldo(id, saldoAlterar);

        entityManager.clear();
        return mapper.toResponse(getModel(id));
    }

    @Transactional
    public ContaResponseDTO alterarTipoConta(Long id, String tipoContaString){
        Conta conta = getModel(id);
        checkInativo(conta.getAtivo());
        TipoConta tipoConta = checkTipoConta(tipoContaString);

        repo.alterarTipoConta(conta.getId(), tipoConta.name());

        entityManager.clear();
        return mapper.toResponse(getModel(id));
    }

    //    Transação
    @Transactional
    public List<ContaResponseDTO> transferencia(Long idOrigem, Long idDestino, BigDecimal valor){
        if (idDestino.equals(idOrigem)) throw new SameAccountTransferException();
        if (valor.compareTo(BigDecimal.ZERO) <= 0) throw new NegativeOrZeroTransferException();

        Conta contaOrigem = getModel(idOrigem);
        Conta contaDestino = getModel(idDestino);

        checkInativo(contaOrigem.getAtivo());
        checkInativo(contaDestino.getAtivo());

        // Se transferência resultar em menos de R$-200,00 na conta origem, lança exceção
        BigDecimal saldoAlteradoOrigem = contaOrigem.getSaldo().subtract(valor);
        if (saldoAlteradoOrigem.compareTo(limiteNegativo) < 0) throw new InsufficientBalanceException();

        repo.transferencia(idOrigem, idDestino, valor);
        entityManager.clear();

        return List.of(
                mapper.toResponse(getModel(idOrigem)),
                mapper.toResponse(getModel(idDestino))
        );
    }

    private TipoConta checkTipoConta(String tipoString){
        tipoString = StringUtils.stripAccents(tipoString);
        if (!EnumUtils.isValidEnumIgnoreCase(TipoConta.class, tipoString)) throw new InvalidAccountTypeException();

        return EnumUtils.getEnumIgnoreCase(TipoConta.class, tipoString);
    }
}
