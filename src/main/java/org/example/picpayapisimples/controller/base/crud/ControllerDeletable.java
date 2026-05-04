package org.example.picpayapisimples.controller.base.crud;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ControllerDeletable {
    @DeleteMapping("/{id}")
    ResponseEntity<Void> excluir(@PathVariable @Positive Long id);
}
