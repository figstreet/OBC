package com.figstreet.service.rest.v1;

import com.figstreet.core.Logging;
import com.figstreet.service.rest.v1.data.ClientListApiData;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class ClientListResource extends ServerResource {
    public static final String LOGGING_NAME = ClientListResource.class.getPackage().getName() + ".ClientListResource";
    public static final String URI_PATH = "clients";


    @Override
    protected void doInit() {
        // nothing to do
    }

    @Get
    public ClientListApiData getClientList() {
        Logging.debugBegin(LOGGING_NAME, "getClientList");

        return new ClientListApiData();
//        try {
//
//        } catch (SQLException e) {
//            throw new ServerException(e);
//        }
    }

}
