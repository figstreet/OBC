package com.figstreet.service.rest.v1;

import com.figstreet.core.ClientID;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.Logging;
import com.figstreet.core.RecordNotExistException;
import com.figstreet.data.client.Client;
import com.figstreet.service.exception.ClientCommsException;
import com.figstreet.service.exception.NotFoundException;
import com.figstreet.service.exception.ServerException;
import com.figstreet.service.rest.v1.data.ClientApiData;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import java.sql.SQLException;


public class ClientServerResource extends ServerResource {
    public static final String LOGGING_NAME = ClientServerResource.class.getPackage().getName() + ".ClientServerResource";
    public static final String ID_PARAM = "id";

    private ClientID fClientID;

    @Override
    protected void doInit() {
        String id = getAttribute(ID_PARAM);
        if (CompareUtil.isEmpty(id)) {
            Logging.warn(LOGGING_NAME, "doInit", "ClientServerResource called with no ID.");
        } else {
            Logging.info(LOGGING_NAME, "doInit", "ClientServerResource called for ID: " + id);
            this.fClientID = new ClientID(id);
        }
    }

    @Get
    public ClientApiData getClient() {
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
    public void storeClient(Client client) {

    }

    @Delete
    public void removeClient() {

    }
}
