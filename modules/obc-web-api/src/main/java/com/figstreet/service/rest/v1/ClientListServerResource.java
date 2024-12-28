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
import com.figstreet.service.rest.v1.data.ClientListApiData;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.sql.SQLException;


public class ClientListServerResource extends ServerResource {
    public static final String LOGGING_NAME = ClientListServerResource.class.getPackage().getName() + ".ClientServerResource";
    public static final String URI_PATH = "clients";
    public static final String ID_PARAM = "id";

    private ClientID fClientID;

    @Override
    protected void doInit() {
        // nothing to do
    }

    @Get
    public ClientListApiData getClientList() {
        Logging.debugBegin(LOGGING_NAME, "getClientList");

        try {
            Client client = Client.getByClientID(this.fClientID);
            return new ClientApiData(client);
        } catch (SQLException e) {
            throw new ServerException(e);
        } catch (RecordNotExistException e) {
            throw new NotFoundException("No record found with ID " + this.fClientID);
        }
    }

}
