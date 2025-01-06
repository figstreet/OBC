package com.figstreet.service.rest.v1;

import com.figstreet.service.rest.ApiUtils;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class PingResource extends ServerResource {
    public static final String URI_PATH = "ping";

    @Get
    @Put
    @Post
    public Representation respond() {
        Variant preferredVariant = getPreferredVariant(ApiUtils.PREFERRED_VARIANT_LIST);
        if (preferredVariant != null) {
            if (ApiUtils.JSON_VARIANT.isCompatible(preferredVariant)) {
                JSONObject json = new JSONObject();
                json.put("response", "pong");
                return new JsonRepresentation(json);
            }

            if (ApiUtils.XML_VARIANT.isCompatible(preferredVariant)) {
                return new StringRepresentation("<response>pong</response>", MediaType.APPLICATION_XML);
            }
        }

        return new StringRepresentation("pong", MediaType.TEXT_PLAIN);
    }
}
