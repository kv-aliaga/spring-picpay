package org.example.picpayapisimples.repository;

import org.example.picpayapisimples.model.entity.Cliente;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends BaseRepository<Cliente> {
//    Exists para verificar UNIQUE
    Boolean existsClienteByCpf(String cpf);
    Boolean existsClienteByTelefone(String telefone);
    Boolean existsClienteByEmail(String email);

//    Procedures
    @Modifying
    @Query(value = "CALL pc_inativar_cliente(:p_id_cliente)", nativeQuery = true)
    void inativarCliente(@Param("p_id_cliente") Long idCliente);
}
