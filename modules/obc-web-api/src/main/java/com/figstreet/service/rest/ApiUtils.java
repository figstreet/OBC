package com.figstreet.service.rest;

import com.figstreet.service.exception.InvalidEntityException;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class ApiUtils {
    public static void checkRole(ServerResource pServerResource, String pRole)
            throws ResourceException {
        if (!pServerResource.isInRole(pRole)) {
            throw new ResourceException(
                    Status.CLIENT_ERROR_FORBIDDEN.getCode(),
                    "You're not authorized to send this call.");
        }
    }

    /**
     * Returns the URL of the resource that represents an instance of Users.
     *
     * @param id
     *            The identifier of the user.
     * @return The URL of the resource that represents an instance of Users.
     */
    public static String getUsersUrl(String id) {
        return "/users/" + id;
    }

    /**
     * Returns the URL of the resource that represents a client.
     *
     * @param id
     *            The id of the client.
     * @return The URL of the resource that represents a client.
     */
    public static String getClientUrl(String id) {
        return "/client/" + id;
    }

    /**
     * Checks that the given entity is not null.
     *
     * @param entity
     *            The entity to check.
     * @throws InvalidEntityException
     *             In case the entity is null.
     */
    public static void notNull(Object entity) throws InvalidEntityException {
        if (entity == null) {
            throw new InvalidEntityException("No input entity");
        }
    }

}
