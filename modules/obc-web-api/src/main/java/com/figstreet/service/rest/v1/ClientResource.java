package com.figstreet.service.rest.v1;

import com.figstreet.core.ClientID;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.Logging;
import com.figstreet.core.RecordNotExistException;
import com.figstreet.data.client.Client;
import com.figstreet.service.exception.ClientCommsException;
import com.figstreet.service.exception.NotFoundException;
import com.figstreet.service.exception.ServerException;
import com.figstreet.service.rest.RestServerResource;
import com.figstreet.service.rest.v1.data.ClientApiData;
import org.restlet.representation.Representation;
import org.restlet.resource.*;

import java.io.IOException;
import java.sql.SQLException;


public class ClientResource extends RestServerResource {
    public static final String LOGGING_NAME = ClientResource.class.getPackage().getName() + ".ClientResource";
    public static final String URI_PATH = "client";
    public static final String ID_PARAM = "id";

    private ClientID fClientID;

    public String getLoggingName() {
        return LOGGING_NAME;
    }

    @Override
    protected void doInit() {
        String id = getAttribute(ID_PARAM);
        if (CompareUtil.isEmpty(id)) {
            Logging.warn(LOGGING_NAME, "doInit", "ClientResource called with no ID.");
        } else {
            try {
                Logging.info(LOGGING_NAME, "doInit", "ClientResource called for ID: " + id);
                this.fClientID = new ClientID(id);
            } catch (Exception e) {
                String msg = "Error parsing ClientID: " + id;
                Logging.error(LOGGING_NAME, "doInit", msg, e);
                throw new ClientCommsException(msg);
            }

        }
    }

    @Get
    public ClientApiData getClient()
            throws ClientCommsException, ServerException, NotFoundException {
        Logging.debugBegin(LOGGING_NAME, "getClient");
        if (this.fClientID == null) {
            String msg = "Method called with no ClientID.";
            Logging.error(LOGGING_NAME, "getClient", msg);
            throw new ClientCommsException(msg);
        }

        //TODO - check permissions

        try {
            Client client = Client.getByClientID(this.fClientID);
            return new ClientApiData(client);
        } catch (SQLException e) {
            throw new ServerException(e);
        } catch (RecordNotExistException e) {
            throw new NotFoundException("No record found with ID " + this.fClientID);
        }
    }

    @Put
    public void updateClient(Representation entity)
            throws ClientCommsException, ServerException, NotFoundException {
        Logging.debugBegin(LOGGING_NAME, "updateClient");
        String requestText = super.extractRequestText(entity, "updateClient");
        if (requestText == null) {
            throw new ClientCommsException("No data sent by client for PUT");
        }

        //TODO
    }

    @Post
    public void addClient(Representation entity)
            throws ClientCommsException, ServerException, NotFoundException {
        Logging.debugBegin(LOGGING_NAME, "addClient");
        String requestText = super.extractRequestText(entity, "addClient");
        if (requestText == null) {
            throw new ClientCommsException("No data sent by client for POST");
        }
    }

    @Delete
    public void removeClient()
            throws ClientCommsException, ServerException, NotFoundException {
        Logging.debugBegin(LOGGING_NAME, "removeClient");
        if (this.fClientID == null) {
            String msg = "Method called with no ClientID.";
            Logging.error(LOGGING_NAME, "removeClient", msg);
            throw new ClientCommsException(msg);
        }
    }
}
