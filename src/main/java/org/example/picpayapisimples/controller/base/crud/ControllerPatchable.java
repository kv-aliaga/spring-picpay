package org.example.picpayapisimples.controller.base.crud;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ControllerPatchable<RESP> {
    @PatchMapping("/{id}")
    ResponseEntity<RESP> patch(@PathVariable @Positive Long id, @RequestBody Map<String, Object> campos);
}
