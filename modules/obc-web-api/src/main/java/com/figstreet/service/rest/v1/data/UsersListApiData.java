package com.figstreet.service.rest.v1.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class UsersListApiData {

    private List<UsersApiData> list;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "user")
    public List<UsersApiData> getList() {
        if (list == null) {
            synchronized (this) {
                if (list == null)
                    list = new ArrayList<>();
            }
        }
        return list;
    }

}
