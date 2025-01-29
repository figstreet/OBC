package com.figstreet.service.rest.v1;

import com.figstreet.core.Logging;
import com.figstreet.data.client.Client;
import com.figstreet.data.client.ClientList;
import com.figstreet.service.exception.ServerException;
import com.figstreet.service.rest.ApiUtils;
import com.figstreet.service.rest.v1.data.ClientApiData;
import com.figstreet.service.rest.v1.data.ClientListApiData;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.sql.SQLException;


public class ClientListResourceV1 extends ServerResource {
    public static final String LOGGING_NAME = ClientListResourceV1.class.getPackage().getName() + ".ClientListResource";
    public static final String URI_PATH = "clients";
    public static final String ACTIVE_ONLY_PARAM = "activeOnly";

    private boolean fActiveOnly;


    @Override
    protected void doInit() {
        String activeOnly = ApiUtils.findQueryParameter(this,
                ACTIVE_ONLY_PARAM, "true");
        this.fActiveOnly = Boolean.parseBoolean(activeOnly);
    }

    @Get
    public ClientListApiData getClientList() {
        Logging.debugBegin(LOGGING_NAME, "getClientList");

        // TODO check permissions

        try {
            ClientList clientList = ClientList.loadAll(this.fActiveOnly);
            ClientListApiData apiList = new ClientListApiData(clientList.size());
            for (Client client : clientList) {
                ClientApiData apiData = new ClientApiData(client);
                apiList.add(apiData);
            }
            return apiList;
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

}
