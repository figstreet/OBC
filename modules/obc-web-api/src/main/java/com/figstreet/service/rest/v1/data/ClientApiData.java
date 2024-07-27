package com.figstreet.service.rest.v1.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.figstreet.core.ClientID;
import com.figstreet.data.client.Client;
import com.figstreet.data.users.UsersID;
import com.figstreet.service.rest.ApiData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.Timestamp;

@JacksonXmlRootElement(localName = ClientApiData.NODE_NAME)
@JsonInclude(Include.NON_NULL)
public class ClientApiData extends ApiData {
    public static final String NODE_NAME = "client";
    public static final String LIST_NAME = "clients";
    public static final String ACTIVE_NODE = "active";
    public static final String NAME_NODE = "name";

    private Client fClient;

    public ClientApiData(Client pClient) {
        this.fClient = pClient;
    }

    @JsonGetter(ACTIVE_NODE)
    public boolean isActive()
    {
        return this.fClient.isActive();
    }

    @JsonGetter(NAME_NODE)
    public String getName()
    {
        return this.fClient.getName();
    }

    @JsonGetter(RECORD_ID_NODE)
    public ClientID getRecordID()
    {
        return this.fClient.getRecordID();
    }

    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    @Override
    public Timestamp getAdded()
    {
        return this.fClient.getAdded();
    }

    @Override
    public UsersID getAddedBy()
    {
        return this.fClient.getAddedBy();
    }

    @Override
    public Timestamp getLastUpdated()
    {
        return this.fClient.getLastUpdated();
    }

    @Override
    public UsersID getLastUpdatedBy()
    {
        return this.fClient.getLastUpdatedBy();
    }

    @Override
    public void append(Element element) {
        Document doc = element.getOwnerDocument();
        Element elmClient = doc.createElement(NODE_NAME);
        element.appendChild(elmClient);

        Element elmId = doc.createElement(RECORD_ID_NODE);
        elmId.appendChild(doc.createTextNode(ClientID.asString(this.getRecordID())));
        elmClient.appendChild(elmId);

        Element elmActive = doc.createElement(ACTIVE_NODE);
        elmActive.appendChild(doc.createTextNode(String.valueOf(this.isActive())));
        elmClient.appendChild(elmActive);

        Element elmName = doc.createElement(NAME_NODE);
        elmName.appendChild(doc.createTextNode(this.getName()));
        elmClient.appendChild(elmName);

        super.append(elmClient);
    }
}
