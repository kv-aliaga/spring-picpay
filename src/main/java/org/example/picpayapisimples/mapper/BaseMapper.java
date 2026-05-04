package org.example.picpayapisimples.mapper;

import java.util.List;

public interface BaseMapper<REQ, RESP, MOD> {
    MOD toModel(REQ dto);
    RESP toResponse(MOD mod);
    default List<RESP> toResponseList(List<MOD> modList){
        return modList
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
