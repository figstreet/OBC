package com.figstreet.service.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

public abstract class ListApiData<E extends ApiData> extends ArrayList<E> {

    public ListApiData() {
        super();
    }

    public ListApiData(int initialSize) {
        super(initialSize);
    }

    @JsonIgnore
    public abstract String getListNodeName();

    public void append(Document document) {
        Element elmList = document.createElement(this.getListNodeName());
        document.appendChild(elmList);

        for (ApiData apiData : this) {
            if (apiData != null)
                apiData.append(elmList);
        }
    }

}
