package com.figstreet.service.rest.v1;

import com.figstreet.core.*;
import com.figstreet.data.client.Client;
import com.figstreet.data.users.UsersID;
import com.figstreet.service.exception.ClientCommsException;
import com.figstreet.service.exception.NotFoundException;
import com.figstreet.service.exception.ServerException;
import com.figstreet.service.rest.ApiUtils;
import com.figstreet.service.rest.RestServerResource;
import com.figstreet.service.rest.v1.data.ClientApiData;
import org.json.JSONObject;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.*;


public class ClientResource extends RestServerResource {
    public static final String LOGGING_NAME = ClientResource.class.getPackage().getName() + ".ClientResource";
    public static final String URI_PATH = "client";
    public static final String ID_PARAM = "id";

    private ClientID fClientID;
    private Variant fPreferredVariant;

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

        this.fPreferredVariant = getPreferredVariant(ApiUtils.PREFERRED_VARIANT_LIST);
    }

    @Get
    public Representation getClient()
            throws ClientCommsException, ServerException, NotFoundException {
        Logging.debugBegin(LOGGING_NAME, "getClient");
        if (this.fClientID == null) {
            String msg = "Method called with no ClientID.";
            Logging.error(LOGGING_NAME, "getClient", msg);
            throw new ClientCommsException(msg);
        }

        //TODO - check permissions

        try {
//            Client client = Client.getByClientID(this.fClientID);
            Client client = new Client("Test", UsersID.ADMIN);
            client.setRecordID(this.fClientID);
            client.setActive(true);
            client.setAdded(DateUtil.now());
            client.setAddedBy(UsersID.ADMIN);
            ClientApiData apiData = new ClientApiData(client);

            if (ApiUtils.JSON_VARIANT.isCompatible(this.fPreferredVariant)) {
                return new JacksonRepresentation<ClientApiData>(apiData);
            }

            return new StringRepresentation(ApiUtils.asXmlString(apiData));
        }
//        } catch (SQLException e) {
//            throw new ServerException(e);
//        } catch (RecordNotExistException e) {
//            throw new NotFoundException("No record found with ID " + this.fClientID);
//        }
        catch (Exception e) {
            throw new NotFoundException(e.getMessage());
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
