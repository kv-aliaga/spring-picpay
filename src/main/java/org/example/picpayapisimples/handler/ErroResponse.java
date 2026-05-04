package org.example.picpayapisimples.handler;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
    LocalDateTime timestamp,
    List<String> mensagens,
    Integer status
){
    public ErroResponse(List<String> mensagens, Integer status) {
        this(LocalDateTime.now(), mensagens, status);
    }
}