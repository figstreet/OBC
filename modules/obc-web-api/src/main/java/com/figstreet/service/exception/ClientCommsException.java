package com.figstreet.service.exception;

import com.figstreet.core.ActionResult;
import org.apache.http.HttpStatus;
import org.restlet.resource.Status;

@Status(HttpStatus.SC_BAD_REQUEST)
public class ClientCommsException extends ApiException {

    public ClientCommsException(String pMessage) {
        super(HttpStatus.SC_BAD_REQUEST, pMessage);
    }

    public ClientCommsException(ActionResult pResult) {
        super(HttpStatus.SC_BAD_REQUEST, pResult);
    }
}