package com.figstreet.service.rest.v1.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.figstreet.core.ClientID;
import com.figstreet.data.client.Client;
import com.figstreet.data.users.UsersID;
import java.sql.Timestamp;

@JacksonXmlRootElement(localName = "client")
@JsonInclude(Include.NON_NULL)
public class ClientApiData {
    private Client fClient;

    public ClientApiData(Client pClient) {
        this.fClient = pClient;
    }

    public boolean isActive()
    {
        return this.fClient.isActive();
    }

    public String getName()
    {
        return this.fClient.getName();
    }

    public ClientID getRecordID()
    {
        return this.fClient.getRecordID();
    }

    public Timestamp getAdded()
    {
        return this.fClient.getAdded();
    }

    public UsersID getAddedBy()
    {
        return this.fClient.getAddedBy();
    }

    public Timestamp getLastUpdated()
    {
        return this.fClient.getLastUpdated();
    }

    public UsersID getLastUpdatedBy()
    {
        return this.fClient.getLastUpdatedBy();
    }

}
