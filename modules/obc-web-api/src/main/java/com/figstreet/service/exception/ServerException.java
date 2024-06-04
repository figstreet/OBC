package com.figstreet.service.exception;

import com.figstreet.core.ActionResult;
import org.apache.http.HttpStatus;
import org.restlet.resource.Status;

@Status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
public class ServerException extends ApiException {

    public ServerException(String pMessage) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, pMessage);
    }

    public ServerException(Throwable pCause) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, pCause);
    }

    public ServerException(ActionResult pResult) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, pResult);
    }
}