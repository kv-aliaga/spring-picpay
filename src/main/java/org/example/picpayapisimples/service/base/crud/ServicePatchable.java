package org.example.picpayapisimples.service.base.crud;

import java.util.Map;

public interface ServicePatchable<RESP> {
    RESP patch(Long id, Map<String, Object> campos);
}
