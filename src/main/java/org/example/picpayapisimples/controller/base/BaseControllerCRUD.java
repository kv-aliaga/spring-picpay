package org.example.picpayapisimples.controller.base;

import org.example.picpayapisimples.controller.base.crud.ControllerPatchable;

public interface BaseControllerCRUD<REQ, RESP>
extends BaseControllerCRD<REQ, RESP>, ControllerPatchable<RESP> {

}