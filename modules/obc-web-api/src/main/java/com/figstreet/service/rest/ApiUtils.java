package com.figstreet.service.rest;

import com.figstreet.core.DateUtil;
import com.figstreet.service.exception.InvalidEntityException;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.sql.Timestamp;

public class ApiUtils {


    public static String asString(Timestamp timestamp) {
        return DateUtil.formatTimestamp(timestamp, DateUtil.ISO8601_TIMESTAMP_FORMAT);
    }

    public static Timestamp asTimestamp(String value) {
        return DateUtil.parseTimestamp(value, DateUtil.ISO8601_TIMESTAMP_FORMAT);
    }

    public static void checkRole(ServerResource pServerResource, String pRole)
            throws ResourceException {
        if (!pServerResource.isInRole(pRole)) {
            throw new ResourceException(
                    Status.CLIENT_ERROR_FORBIDDEN.getCode(),
                    "You're not authorized to send this call.");
        }
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
