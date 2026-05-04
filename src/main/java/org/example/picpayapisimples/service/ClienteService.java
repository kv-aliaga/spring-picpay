package org.example.picpayapisimples.service;

import org.example.picpayapisimples.dto.cliente.ClienteRequestDTO;
import org.example.picpayapisimples.dto.cliente.ClienteResponseDTO;
import org.example.picpayapisimples.handler.exception.patch.PatchException;
import org.example.picpayapisimples.handler.exception.validation.age.InvalidAgeException;
import org.example.picpayapisimples.handler.exception.patch.UniqueViolationException;
import org.example.picpayapisimples.handler.exception.validation.age.UnrealisticAgeException;
import org.example.picpayapisimples.mapper.ClienteMapper;
import org.example.picpayapisimples.model.entity.Cliente;
import org.example.picpayapisimples.repository.ClienteRepository;
import org.example.picpayapisimples.service.base.BaseServiceCRUD;
import org.example.picpayapisimples.service.base.BaseService;
import org.example.picpayapisimples.utils.PatchConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

@Service
public class ClienteService
extends BaseService<ClienteRequestDTO, ClienteResponseDTO, Cliente>
implements BaseServiceCRUD<ClienteRequestDTO, ClienteResponseDTO> {
    private final ClienteRepository repo;
    private static final PatchConfig patchConfig = new PatchConfig(
            Set.of("cpf", "nome", "email", "telefone", "dataNascimento"),
            Set.of("nome", "email", "telefone")
    );

    public ClienteService(ClienteRepository repo, ClienteMapper mapper){
        super("Cliente", repo, mapper);
        this.repo = repo;
    }

    @Override
    public ClienteResponseDTO inserir(ClienteRequestDTO dto) {
        checkIdadeCliente(dto.getDataNascimento());
        checkCpfExists(dto.getCpf());
        checkEmailExists(dto.getEmail());
        checkTelefoneExists(dto.getTelefone());

        return super.inserir(dto);
    }

    @Override
    public ClienteResponseDTO patch(Long id, Map<String, Object> campos) {
        Cliente cliente = getModel(id);
        checkInativo(cliente.getAtivo());
        checkCamposPatch(patchConfig, campos);

        if (campos.containsKey("nome")) cliente.setNome((String) campos.get("nome"));
        if (campos.containsKey("email")){
            String patchEmail = campos.get("email").toString();

            if (!cliente.getEmail().equals(patchEmail)){
                checkEmailExists(patchEmail);
                cliente.setEmail(patchEmail);
            }
        }
        if (campos.containsKey("telefone")){
            String patchTelefone = campos.get("telefone").toString();

            if (!cliente.getTelefone().equals(patchTelefone)){
                checkTelefoneExists(patchTelefone);
                cliente.setTelefone(patchTelefone);
            }
        }

        Cliente salvo = repo.save(cliente);
        return mapper.toResponse(salvo);
    }

//    Métodos personalizados
    @Transactional
    public void inativarCliente(Long id){
        Cliente cliente = getModel(id);
        if (!cliente.getAtivo()) throw new PatchException("O cliente já está inativo");

        repo.inativarCliente(cliente.getId());
    }

//    Exists para verificar entities
    private void checkCpfExists(String cpf){
        if (repo.existsClienteByCpf(cpf)) throw new UniqueViolationException("CPF", cpf);
    }

    private void checkEmailExists(String email){
        if (repo.existsClienteByEmail(email)) throw new UniqueViolationException("E-mail", email);
    }

    private void checkTelefoneExists(String telefone){
        if (repo.existsClienteByTelefone(telefone)) throw new UniqueViolationException("Telefone", telefone);
    }

    private void checkIdadeCliente(LocalDate dataNascimento){
        Integer idade = Math.toIntExact(ChronoUnit.YEARS.between(dataNascimento, LocalDate.now()));
        if (idade < 18) throw new InvalidAgeException();
        if (idade > 125) throw new UnrealisticAgeException();
    }
}
