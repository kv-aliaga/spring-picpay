package org.example.picpayapisimples.service.base;

import org.example.picpayapisimples.service.base.crud.*;
import org.example.picpayapisimples.service.base.crud.ServiceReadable;

public interface BaseServiceCRD<REQ, RESP>
extends ServiceReadable<RESP>, ServiceCreatable<REQ, RESP>, ServiceDeletable{
}
