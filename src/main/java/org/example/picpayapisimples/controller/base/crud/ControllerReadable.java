package org.example.picpayapisimples.controller.base.crud;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ControllerReadable<RESP> {
    @GetMapping("/{id}")
    ResponseEntity<RESP> buscarPorId(@PathVariable @Positive Long id);

    @GetMapping
    ResponseEntity<List<RESP>> buscarTodos();
}
