package com.figstreet.service.rest.v1;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class RestletApplicationV1 extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/clients/{" + ClientServerResource.ID_PARAM + "}", ClientServerResource.class);

        return router;
    }
}
