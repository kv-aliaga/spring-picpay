-- SET INFORMAÇÕES DE DATA E HORA
ALTER DATABASE db_picpay SET TIMEZONE TO 'America/Sao_Paulo';
ALTER DATABASE db_picpay SET datestyle = 'ISO, DMY';

-- EXCLUI SCHEMAS
DROP SCHEMA IF EXISTS public CASCADE;
DROP SCHEMA IF EXISTS log CASCADE;

-- EXCLUI TABELAS E ÍNDICES
DROP TABLE IF EXISTS tb_cliente;
DROP TABLE IF EXISTS tb_conta;
DROP TABLE IF EXISTS log.tb_log_geral;
DROP TABLE IF EXISTS log.tb_log_conta;
DROP TABLE IF EXISTS log.tb_log_cliente;
DROP INDEX IF EXISTS uq_cliente_email_ativo;
DROP INDEX IF EXISTS uq_cliente_telefone_ativo;
DROP INDEX IF EXISTS uq_cliente_cpf_ativo;

-- CRIA SCHEMAS
CREATE SCHEMA public;
CREATE SCHEMA log;

-- TABELAS PADRÃO DO BANCO (SCHEMA PUBLIC)
CREATE TABLE tb_cliente(
    id BIGSERIAL PRIMARY KEY,
    cpf CHAR(11) NOT NULL UNIQUE CHECK ( cpf ~ '^[0-9]{11}$' ),
    nome VARCHAR(60) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE CHECK ( email ~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$' ),
    telefone VARCHAR(20) NOT NULL UNIQUE CHECK (telefone ~ '^(55)?(?:([1-9]{2})?)([0-9]{4,5})([0-9]{4})$'),
    data_nascimento DATE NOT NULL check ( EXTRACT(YEAR FROM AGE(CURRENT_DATE, data_nascimento)) BETWEEN 18 AND 120),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_conta(
    id BIGSERIAL PRIMARY KEY,
    numero CHAR(5) NOT NULL UNIQUE CHECK ( numero ~ '^[0-9]{5}$' ),
    agencia VARCHAR(60) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    tipo_conta VARCHAR(13) NOT NULL CHECK ( tipo_conta IN ('CORRENTE', 'POUPANCA', 'SALARIO', 'UNIVERSITARIA', 'EMPRESARIAL') ),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    id_cliente INT NOT NULL REFERENCES tb_cliente(id),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABELAS DE LOG (SCHEMA LOG)
CREATE TABLE log.tb_log_geral(
    id BIGSERIAL PRIMARY KEY,
    usuario VARCHAR(60),
    tabela VARCHAR(60),
    acao VARCHAR(8),
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE log.tb_log_cliente(
    id BIGSERIAL PRIMARY KEY,
    id_log_geral INT NOT NULL REFERENCES log.tb_log_geral(id),
    old_nome VARCHAR(60),
    new_nome VARCHAR(60),
    old_email VARCHAR(255),
    new_email VARCHAR(255),
    old_telefone VARCHAR(20),
    new_telefone VARCHAR(20),
    old_ativo BOOLEAN,
    new_ativo BOOLEAN
);

CREATE TABLE log.tb_log_conta(
    id BIGSERIAL PRIMARY KEY,
    id_log_geral INT NOT NULL REFERENCES log.tb_log_geral(id),
    old_agencia VARCHAR(60),
    new_agencia VARCHAR(60),
    old_saldo NUMERIC(15,2),
    new_saldo NUMERIC(15,2),
    old_tipo_conta VARCHAR(13),
    new_tipo_conta VARCHAR(13),
    old_ativo BOOLEAN,
    new_ativo BOOLEAN
);

-- ÍNDICES
-- UNIQUE APENAS PARA CLIENTES ATIVOS
CREATE UNIQUE INDEX uq_cliente_email_ativo
ON tb_cliente(email)
WHERE ativo = true;

CREATE UNIQUE INDEX uq_cliente_telefone_ativo
ON tb_cliente(telefone)
WHERE ativo = true;

-- FUNCTIONS
-- VERIFICAR SE CLIENTE ESTÁ ATIVO
CREATE OR REPLACE FUNCTION fn_cliente_is_ativo(p_id_cliente BIGINT)
RETURNS BOOLEAN
LANGUAGE plpgsql AS $$
    DECLARE
        is_ativo BOOLEAN;

    BEGIN
        SELECT ativo INTO is_ativo FROM tb_cliente
        WHERE id = p_id_cliente;

        RETURN COALESCE(is_ativo, FALSE);
    END;
$$;

-- VERIFICAR SE CONTA ESTÁ ATIVA
CREATE OR REPLACE FUNCTION fn_conta_is_ativo(p_id_conta BIGINT)
    RETURNS BOOLEAN
    LANGUAGE plpgsql AS $$
DECLARE
    is_ativo BOOLEAN;

BEGIN
    SELECT ativo INTO is_ativo FROM tb_conta
    WHERE id = p_id_conta;

    RETURN COALESCE(is_ativo, FALSE);
END;
$$;

-- PROCEDURES
-- INATIVAR CONTAS QUANDO INATIVAR CLIENTE
CREATE OR REPLACE PROCEDURE pc_inativar_cliente(p_id_cliente BIGINT)
    LANGUAGE plpgsql AS $$
BEGIN
    UPDATE tb_conta
    SET ativo = false
    WHERE id_cliente = p_id_cliente;

    UPDATE tb_cliente
    SET ativo = false
    WHERE id = p_id_cliente;
END;
$$;

-- INATIVAR CONTAS
CREATE OR REPLACE PROCEDURE pc_inativar_conta(p_id_conta BIGINT)
LANGUAGE plpgsql AS $$
    BEGIN
        UPDATE tb_conta
        SET ativo = false
        WHERE id = p_id_conta;
    END;
$$;

-- ALTERAR SALDO
CREATE OR REPLACE PROCEDURE pc_alterar_saldo(p_id_conta BIGINT, p_valor_alterar NUMERIC(15, 2))
LANGUAGE plpgsql AS $$
    DECLARE
        v_saldo_atual NUMERIC(15, 2);
        v_saldo_final NUMERIC(15, 2);

   BEGIN
        IF NOT fn_conta_is_ativo(p_id_conta) THEN
            RAISE EXCEPTION 'A conta não está ativa';
        end if;

        IF p_valor_alterar = 0 THEN
            RAISE EXCEPTION 'Valor a alterar não pode ser zero';
        END IF;

       SELECT saldo FROM tb_conta
       WHERE id = p_id_conta
       INTO v_saldo_atual;

       v_saldo_final := v_saldo_atual + p_valor_alterar;

       IF v_saldo_final < -200 THEN
           RAISE EXCEPTION 'Saldo não pode ser menor que R$-200,00';
       end if;

       UPDATE tb_conta
       SET saldo = v_saldo_final
       WHERE id = p_id_conta;
    END;
$$;

-- TRANSFERÊNCIA ENTRE CONTAS
CREATE OR REPLACE PROCEDURE pc_transferencia(p_id_conta_origem BIGINT, p_id_conta_destino BIGINT, p_valor_alterar NUMERIC(15, 2))
LANGUAGE plpgsql AS $$
    DECLARE
        v_saldo_final_origem NUMERIC(15, 2);

    BEGIN
        IF p_valor_alterar <= 0 THEN
            RAISE EXCEPTION 'Valor precisa ser maior que zero';
        end if;

        IF p_id_conta_destino = p_id_conta_origem THEN
            RAISE EXCEPTION 'As contas não podem ser iguais';
        end if;

        IF NOT fn_conta_is_ativo(p_id_conta_origem) THEN
            RAISE EXCEPTION 'A conta de origem não está ativa';
        END IF;

        IF NOT fn_conta_is_ativo(p_id_conta_destino) THEN
            RAISE EXCEPTION 'A conta de destino não está ativa';
        END IF;

        SELECT saldo - p_valor_alterar FROM tb_conta
        WHERE id = p_id_conta_origem
        INTO v_saldo_final_origem;

        IF v_saldo_final_origem < -200 THEN
            RAISE EXCEPTION 'Saldo da conta de origem não pode ser menor que R$-200,00';
        end if;

        UPDATE tb_conta
        SET saldo = saldo - p_valor_alterar
        WHERE id = p_id_conta_origem;

        UPDATE tb_conta
        SET saldo = saldo + p_valor_alterar
        WHERE id = p_id_conta_destino;
    END;
$$;

-- ALTERAR TIPO DA CONTA
CREATE OR REPLACE PROCEDURE pc_alterar_tipo_conta(p_id_conta BIGINT, p_tipo_conta VARCHAR(13))
LANGUAGE plpgsql AS $$
    BEGIN
        IF NOT fn_conta_is_ativo(p_id_conta) THEN
            RAISE EXCEPTION 'A conta não está ativa';
        end if;

        IF p_tipo_conta NOT IN ('CORRENTE', 'POUPANCA', 'SALARIO', 'UNIVERSITARIA', 'EMPRESARIAL') THEN
            RAISE EXCEPTION 'Tipo da conta inválido';
        END IF;

        UPDATE tb_conta SET tipo_conta = p_tipo_conta
        WHERE id = p_id_conta;
    END;
$$;

-- TRIGGERS
-- VERIFICAR SE CLIENTE ESTÁ ATIVO ANTES DE CRIAR CONTA
CREATE OR REPLACE FUNCTION fn_validar_cliente_ativo_antes_insert_conta()
RETURNS TRIGGER
LANGUAGE plpgsql AS $$
    BEGIN
        IF NOT fn_cliente_is_ativo(NEW.id_cliente) THEN
            RAISE EXCEPTION 'Não é possível utilizar dados da conta de um cliente inativo';
        end if;

        RETURN NEW;
    END;
$$;

CREATE OR REPLACE TRIGGER tg_validar_cliente_ativo_antes_insert_conta
BEFORE UPDATE OR INSERT ON tb_conta
FOR EACH ROW EXECUTE FUNCTION fn_validar_cliente_ativo_antes_insert_conta();

-- MUDANÇA AUTOMÁTICA DE DATA_ATUALIZACAO
CREATE OR REPLACE FUNCTION fn_upd_data_atualizacao()
RETURNS TRIGGER
LANGUAGE plpgsql AS $$
    BEGIN
        NEW.data_atualizacao = CURRENT_TIMESTAMP;
        RETURN NEW;
    END;
$$;

CREATE OR REPLACE TRIGGER tg_atualizacao_conta
BEFORE UPDATE ON tb_conta
FOR EACH ROW EXECUTE FUNCTION fn_upd_data_atualizacao();

CREATE OR REPLACE TRIGGER tg_atualizacao_conta
BEFORE UPDATE ON tb_cliente
FOR EACH ROW EXECUTE FUNCTION fn_upd_data_atualizacao();

-- TRIGGERS DE AUDITORIA
CREATE OR REPLACE FUNCTION log.fn_auditoria_cliente()
RETURNS TRIGGER
LANGUAGE plpgsql AS $$
    DECLARE
        v_id_log INT;

    BEGIN
        INSERT INTO log.tb_log_geral(usuario, tabela, acao)
        VALUES (current_user, tg_table_name, tg_op)
        RETURNING id INTO v_id_log;

        IF tg_op = 'UPDATE' THEN
            INSERT INTO log.tb_log_cliente(id_log_geral, old_nome, new_nome, old_email, new_email, old_telefone, new_telefone, old_ativo, new_ativo)
            VALUES (v_id_log, OLD.nome, NEW.nome, OLD.email, NEW.email, OLD.telefone, NEW.telefone, OLD.ativo, NEW.ativo);
        ELSIF tg_op = 'INSERT' THEN
            INSERT INTO log.tb_log_cliente(id_log_geral, new_nome, new_email, new_telefone, new_ativo)
            VALUES (v_id_log , NEW.nome, NEW.email, NEW.telefone, NEW.ativo);
        ELSE
            INSERT INTO log.tb_log_cliente(id_log_geral, old_nome, old_email, old_telefone, old_ativo)
            VALUES (v_id_log, OLD.nome, OLD.email, OLD.telefone, OLD.ativo);
        END IF;

        IF tg_op = 'DELETE' THEN
            RETURN OLD;
        ELSE
            RETURN NEW;
        END IF;
    END;
$$;

CREATE OR REPLACE FUNCTION log.fn_auditoria_conta()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS $$
DECLARE
    v_id_log INT;

BEGIN
    INSERT INTO log.tb_log_geral(usuario, tabela, acao)
    VALUES (current_user, tg_table_name, tg_op)
    RETURNING id INTO v_id_log;

    IF tg_op = 'UPDATE' THEN
        INSERT INTO log.tb_log_conta(id_log_geral, old_agencia, new_agencia, old_saldo, new_saldo, old_tipo_conta, new_tipo_conta, old_ativo, new_ativo)
        VALUES (v_id_log, OLD.agencia, NEW.agencia, OLD.saldo, NEW.saldo, OLD.tipo_conta, NEW.tipo_conta, OLD.ativo, NEW.ativo);
    ELSIF tg_op = 'INSERT' THEN
        INSERT INTO log.tb_log_conta(id_log_geral, new_agencia, new_saldo, new_tipo_conta, new_ativo)
        VALUES (v_id_log, NEW.agencia, NEW.saldo, NEW.tipo_conta, NEW.ativo);
    ELSE
        INSERT INTO log.tb_log_conta(id_log_geral, old_agencia, old_saldo, old_tipo_conta, old_ativo)
        VALUES (v_id_log, OLD.agencia, OLD.saldo, OLD.tipo_conta, OLD.ativo);
    END IF;

    IF tg_op = 'DELETE' THEN
        RETURN OLD;
    ELSE
        RETURN NEW;
    END IF;
END;
$$;

CREATE OR REPLACE TRIGGER tg_auditoria_cliente
AFTER INSERT OR DELETE OR UPDATE ON tb_cliente
FOR EACH ROW EXECUTE FUNCTION log.fn_auditoria_cliente();

CREATE OR REPLACE TRIGGER tg_auditoria_conta
AFTER INSERT OR UPDATE OR DELETE ON tb_conta
FOR EACH ROW EXECUTE FUNCTION log.fn_auditoria_conta();

-- INATIVAR AUTOMATICAMENTE APÓS SEIS MESES SEM ALTERAÇÃO NA DATA DE ATUALIZAÇÃO
CREATE OR REPLACE FUNCTION fn_cliente_inativar_automaticamente()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS $$
DECLARE
    v_seis_meses_atras DATE;

BEGIN
    SELECT (NOW() - INTERVAL '5 months')::date into v_seis_meses_atras;

    UPDATE tb_cliente
    SET ativo = FALSE
    WHERE ativo = TRUE AND data_atualizacao::date <= v_seis_meses_atras;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION fn_conta_inativar_automaticamente()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS $$
DECLARE
    v_seis_meses_atras DATE;

BEGIN
    SELECT (NOW() - INTERVAL '5 months')::date into v_seis_meses_atras;

    UPDATE tb_conta
    SET ativo = FALSE
    WHERE ativo = TRUE AND data_atualizacao::date <= v_seis_meses_atras;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER tg_cliente_inativar_automaticamente
AFTER INSERT ON log.tb_log_geral
FOR EACH ROW EXECUTE FUNCTION fn_cliente_inativar_automaticamente();

CREATE OR REPLACE TRIGGER tg_conta_inativar_automaticamente
AFTER INSERT ON log.tb_log_geral
FOR EACH ROW EXECUTE FUNCTION fn_conta_inativar_automaticamente();


-- DATALOAD
INSERT INTO tb_cliente
(cpf, nome, email, telefone, data_nascimento, ativo)
VALUES
    ('12345678901', 'João Silva', 'joao.silva@email.com', '11987654321', '1995-04-10', TRUE),
    ('98765432100', 'Maria Souza', 'maria.souza@email.com', '11976543210', '1998-08-22', TRUE),
    ('11122233344', 'Carlos Oliveira', 'carlos.oliveira@email.com', '21987651234', '1990-01-15', TRUE),
    ('55566677788', 'Ana Santos', 'ana.santos@email.com', '31991234567', '1987-11-03', TRUE),
    ('99988877766', 'Pedro Lima', 'pedro.lima@email.com', '41987654321', '2001-06-30', TRUE),
    ('22233344455', 'Fernanda Costa', 'fernanda.costa@email.com', '51999887766', '1999-12-12', TRUE),
    ('33344455566', 'Lucas Almeida', 'lucas.almeida@email.com', '61988776655', '1994-03-25', TRUE),
    ('44455566677', 'Juliana Rocha', 'juliana.rocha@email.com', '71987654321', '1992-09-18', TRUE),
    ('66677788899', 'Rafael Martins', 'rafael.martins@email.com', '81991239876', '1985-07-07', TRUE),
    ('77788899900', 'Camila Ferreira', 'camila.ferreira@email.com', '91981234567', '2000-02-20', TRUE);

INSERT INTO tb_conta
(numero, agencia, saldo, tipo_conta, ativo, id_cliente)
VALUES
    ('10001', 'Agência São Paulo Centro', 1500.00, 'CORRENTE', TRUE, 1),
    ('10002', 'Agência São Paulo Centro', 2300.50, 'POUPANCA', TRUE, 2),
    ('10003', 'Agência Rio de Janeiro', 750.25, 'SALARIO', TRUE, 3),
    ('10004', 'Agência Belo Horizonte', 12000.00, 'EMPRESARIAL', TRUE, 4),
    ('10005', 'Agência Curitiba', 300.00, 'UNIVERSITARIA', TRUE, 5),
    ('10006', 'Agência Porto Alegre', 980.90, 'CORRENTE', TRUE, 6),
    ('10007', 'Agência Brasília', 4500.75, 'POUPANCA', TRUE, 7),
    ('10008', 'Agência Salvador', 125.40, 'UNIVERSITARIA', TRUE, 8),
    ('10009', 'Agência Recife', 8900.00, 'EMPRESARIAL', TRUE, 9),
    ('10010', 'Agência Belém', 640.35, 'SALARIO', TRUE, 10);