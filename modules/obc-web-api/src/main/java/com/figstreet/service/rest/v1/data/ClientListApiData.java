package com.figstreet.service.rest.v1.data;

import org.w3c.dom.Document;
import com.figstreet.core.ClientID;
import com.figstreet.data.client.Client;
import com.figstreet.data.users.UsersID;
import com.figstreet.service.rest.ApiUtils;
import com.figstreet.service.rest.ListApiData;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ClientListApiData extends ListApiData<ClientApiData> {
    public static final String LIST_NAME = "clients";

    public ClientListApiData() {
        super();
    }

    public ClientListApiData(int initialSize) {
        super(initialSize);
    }

    @Override
    public String getListNodeName() {
        return LIST_NAME;
    }


    public static void main(String[] args) throws Exception {
        Client test1 = new Client("Test 1", UsersID.ADMIN);
        test1.setRecordID(new ClientID(1234));
        Client test2 = new Client("Test 2", UsersID.ADMIN);
        test2.setRecordID(new ClientID(2345));
        Client test3 = new Client("Test 3", UsersID.ADMIN);
        test3.setRecordID(new ClientID(3456));

        ClientApiData clientApiData1 = new ClientApiData(test1);
        ClientApiData clientApiData2 = new ClientApiData(test2);
        ClientApiData clientApiData3 = new ClientApiData(test3);

        ClientListApiData clientListApiData = new ClientListApiData(3);
        clientListApiData.add(clientApiData1);
        clientListApiData.add(clientApiData2);
        clientListApiData.add(clientApiData3);

        System.out.println("Number of clients: " + clientListApiData.size());

        Document xmlDoc = ApiUtils.asXmlDocument(clientListApiData);
        Path tester = Paths.get("clientlist.xml");
        try (OutputStream os = Files.newOutputStream(tester)) {
            ApiUtils.outputXml(xmlDoc, os);
        }

    }

}
