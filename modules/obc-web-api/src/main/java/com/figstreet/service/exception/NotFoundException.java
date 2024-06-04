package com.figstreet.service.exception;

import com.figstreet.core.ActionResult;
import org.apache.http.HttpStatus;
import org.restlet.resource.Status;

@Status(HttpStatus.SC_NOT_FOUND)
public class NotFoundException extends ApiException {

    public NotFoundException(String pMessage) {
        super(HttpStatus.SC_NOT_FOUND, pMessage);
    }

    public NotFoundException(ActionResult pResult) {
        super(HttpStatus.SC_NOT_FOUND, pResult);
    }
}