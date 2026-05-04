package org.example.picpayapisimples.utils;

import java.util.HashSet;
import java.util.Set;

// Evita falhas silenciosas quando o usuário envia um campo que:
// Ou não existe na tabela
// Ou é imutável
public record PatchConfig(
        Set<String> allCampos,
        Set<String> patchableCampos
){
    public PatchConfig {
        allCampos = new HashSet<>(allCampos);

        // Campos vindos do BaseModel
        allCampos.addAll(Set.of("id", "ativo", "dataCriacao", "dataAtualizacao"));
    }
}