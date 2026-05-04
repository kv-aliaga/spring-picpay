package org.example.picpayapisimples.service.base;

import org.example.picpayapisimples.service.base.crud.ServicePatchable;

public interface BaseServiceCRUD<REQ, RESP>
extends BaseServiceCRD<REQ, RESP>, ServicePatchable<RESP> {
}
