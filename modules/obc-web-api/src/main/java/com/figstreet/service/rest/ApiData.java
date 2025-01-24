package com.figstreet.service.rest;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.figstreet.data.users.UsersID;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.Timestamp;

public abstract class ApiData {
    public static final String RECORD_ID_NODE = "id";
    public static final String ADDED_TIMESTAMP_NODE = "added";
    public static final String ADDED_BY_NODE = "addedBy";
    public static final String LASTUPDATED_TIMESTAMP_NODE = "lastUpdated";
    public static final String LASTUPDATED_BY_NODE = "lastUpdatedBy";

    @JsonIgnore
    public abstract String getNodeName();

    @JsonGetter(ADDED_TIMESTAMP_NODE)
    public abstract String getAdded();

    @JsonGetter(ADDED_BY_NODE)
    public abstract String getAddedBy();

    @JsonGetter(LASTUPDATED_TIMESTAMP_NODE)
    public abstract String getLastUpdated();

    @JsonGetter(LASTUPDATED_BY_NODE)
    public abstract String getLastUpdatedBy();


    public void appendTo(Element element) {
        Document doc = element.getOwnerDocument();

        String addedTimestamp = this.getAdded();
        if (addedTimestamp != null) {
            Element addedTS = doc.createElement(ADDED_TIMESTAMP_NODE);
            addedTS.appendChild(doc.createTextNode(addedTimestamp));
            element.appendChild(addedTS);
        }

        String addedUsersID = this.getAddedBy();
        if (addedUsersID != null) {
            Element addedBy = doc.createElement(ADDED_BY_NODE);
            addedBy.appendChild(doc.createTextNode(addedUsersID));
            element.appendChild(addedBy);
        }

        String luTimestamp = this.getLastUpdated();
        if (luTimestamp != null) {
            Element luTS = doc.createElement(LASTUPDATED_TIMESTAMP_NODE);
            luTS.appendChild(doc.createTextNode(luTimestamp));
            element.appendChild(luTS);
        }

        String luUserID = this.getLastUpdatedBy();
        if (luUserID != null) {
            Element luBy = doc.createElement(LASTUPDATED_BY_NODE);
            luBy.appendChild(doc.createTextNode(luUserID));
            element.appendChild(luBy);
        }
    }
}
