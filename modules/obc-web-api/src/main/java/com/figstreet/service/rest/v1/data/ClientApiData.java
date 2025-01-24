package com.figstreet.service.rest.v1.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.figstreet.core.ClientID;
import com.figstreet.data.client.Client;
import com.figstreet.data.users.UsersID;
import com.figstreet.service.rest.ApiData;
import com.figstreet.service.rest.ApiUtils;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JacksonXmlRootElement(localName = ClientApiData.NODE_NAME)
@JsonInclude(Include.NON_NULL)
public class ClientApiData extends ApiData {
    public static final String NODE_NAME = "client";
    public static final String ACTIVE_NODE = "active";
    public static final String NAME_NODE = "name";

    private final Client fClient;

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
    public String getRecordID()
    {
        return ClientID.asString(this.fClient.getRecordID());
    }

    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    @Override
    public String getAdded()
    {
        return ApiUtils.asString(this.fClient.getAdded());
    }

    @Override
    public String getAddedBy()
    {
        return UsersID.asString(this.fClient.getAddedBy());
    }

    @Override
    public String getLastUpdated()
    {
        return ApiUtils.asString(this.fClient.getLastUpdated());
    }

    @Override
    public String getLastUpdatedBy()
    {
        return UsersID.asString(this.fClient.getLastUpdatedBy());
    }

    @Override
    public void appendTo(Element element) {
        Document doc = element.getOwnerDocument();

        String id = this.getRecordID();
        if (id != null) {
            Element elmId = doc.createElement(RECORD_ID_NODE);
            elmId.appendChild(doc.createTextNode(id));
            element.appendChild(elmId);
        }

        Element elmActive = doc.createElement(ACTIVE_NODE);
        elmActive.appendChild(doc.createTextNode(String.valueOf(this.isActive())));
        element.appendChild(elmActive);

        String name = this.getName();
        if (name != null) {
            Element elmName = doc.createElement(NAME_NODE);
            elmName.appendChild(doc.createTextNode(name));
            element.appendChild(elmName);
        }

        super.appendTo(element);
    }

    public static void main(String[] args) throws Exception {
        Client test1 = new Client("Test 1", UsersID.ADMIN);
        test1.setRecordID(new ClientID(1234));
        ClientApiData clientApiData1 = new ClientApiData(test1);
        System.out.println("As XML:");
        System.out.println(ApiUtils.asXmlString(clientApiData1));
        System.out.println("\nAs JSON: ");
        JacksonRepresentation<ClientApiData> rep = new JacksonRepresentation<>(clientApiData1);
        System.out.println(rep.getText());
    }
}
