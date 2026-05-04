package org.example.picpayapisimples.repository;

import org.example.picpayapisimples.model.entity.Conta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ContaRepository extends BaseRepository<Conta> {
//    Procedures
    @Modifying
    @Query(value = "CALL pc_inativar_conta(:p_id_conta)", nativeQuery = true)
    void inativarConta(@Param("p_id_conta") Long idConta);

    @Modifying
    @Query(value = "CALL pc_alterar_saldo(:p_id_conta, :p_valor_alterar)", nativeQuery = true)
    void alterarSaldo(@Param("p_id_conta") Long idConta, @Param("p_valor_alterar") BigDecimal valorAlterar);

    @Modifying
    @Query(value = "CALL pc_alterar_tipo_conta(:p_id_conta, :p_tipo_conta)", nativeQuery = true)
    void alterarTipoConta(@Param("p_id_conta") Long idConta, @Param("p_tipo_conta") String tipoConta);

    @Modifying
    @Query(value = "CALL pc_transferencia(:p_id_conta_origem, :p_id_conta_destino, :p_valor_alterar)", nativeQuery = true)
    void transferencia(
            @Param("p_id_conta_origem") Long idOrigem,
            @Param("p_id_conta_destino") Long idDestino,
            @Param("p_valor_alterar") BigDecimal valor
    );
}
