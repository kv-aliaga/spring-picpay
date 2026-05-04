package org.example.picpayapisimples.controller.base;

import org.example.picpayapisimples.controller.base.crud.*;

public interface BaseControllerCRD<REQ, RESP>
extends ControllerReadable<RESP>, ControllerCreatable<REQ, RESP>, ControllerDeletable {
}
