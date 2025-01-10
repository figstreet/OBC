package com.figstreet.service.rest;

import com.figstreet.core.Logging;
import com.figstreet.service.exception.ClientCommsException;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import java.io.IOException;

public abstract class RestServerResource extends ServerResource {

    public abstract String getLoggingName();

    public String extractRequestText(Representation entity, String method) {
        String requestData = null;
        try {
            requestData = entity.getText();
        } catch(IOException e) {
            String msg = "Error extracting text submitted by client";
            Logging.error(this.getLoggingName(), method, msg);
        }
        return requestData;
    }
}
