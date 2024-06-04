package com.figstreet.service.exception;


import com.figstreet.core.ActionResult;
import org.apache.http.HttpStatus;
import org.restlet.resource.Status;

@Status(HttpStatus.SC_UNPROCESSABLE_ENTITY)
public class InvalidEntityException extends ApiException {

    public InvalidEntityException(String pMessage) {
        super(HttpStatus.SC_UNPROCESSABLE_ENTITY, pMessage);
    }

    public InvalidEntityException(ActionResult pResult) {
        super(HttpStatus.SC_UNPROCESSABLE_ENTITY, pResult);
    }

}
