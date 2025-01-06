package com.figstreet.service.rest.v1;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestletApplicationV1 extends Application {
    public static final String API_URL_PREFIX = "/";
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach( API_URL_PREFIX + ClientResource.URI_PATH + "/{"
                + ClientResource.ID_PARAM + "}", ClientResource.class);
        router.attach(API_URL_PREFIX + ClientListResource.URI_PATH,
                ClientListResource.class);
        router.attach(API_URL_PREFIX + PingResource.URI_PATH,
                PingResource.class);

        return router;
    }
}
