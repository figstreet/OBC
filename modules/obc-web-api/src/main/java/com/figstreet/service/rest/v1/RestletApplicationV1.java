package com.figstreet.service.rest.v1;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestletApplicationV1 extends Application {
    public static final String API_URL_PREFIX = "/";
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach( API_URL_PREFIX + ClientResourceV1.URI_PATH + "/{"
                + ClientResourceV1.ID_PARAM + "}", ClientResourceV1.class);
        router.attach(API_URL_PREFIX + ClientListResourceV1.URI_PATH,
                ClientListResourceV1.class);
        router.attach(API_URL_PREFIX + PingResource.URI_PATH,
                PingResource.class);

        return router;
    }
}
