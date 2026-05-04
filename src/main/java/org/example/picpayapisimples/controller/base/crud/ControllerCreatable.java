package org.example.picpayapisimples.controller.base.crud;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ControllerCreatable<REQ, RESP> {
    @PostMapping
    ResponseEntity<RESP> inserir(@RequestBody @Valid REQ dto);
}
