/*******************************************************************************
 * Copyright 2009-2016 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * List Inventory Supply By Next Token Response
 * API Version: 2010-10-01
 * Library Version: 2014-09-30
 * Generated: Mon Mar 21 09:01:27 PDT 2016
 */
package com.amazonservices.mws.fulfillmentInventory.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.amazonservices.mws.client.AbstractMwsObject;
import com.amazonservices.mws.client.MwsReader;
import com.amazonservices.mws.client.MwsWriter;

/**
 * ListInventorySupplyByNextTokenResponse complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="ListInventorySupplyByNextTokenResponse"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="ListInventorySupplyByNextTokenResult" type="{http://mws.amazonaws.com/FulfillmentInventory/2010-10-01/}ListInventorySupplyByNextTokenResult" minOccurs="0"/&gt;
 *             &lt;element name="ResponseMetadata" type="{http://mws.amazonaws.com/FulfillmentInventory/2010-10-01/}ResponseMetadata" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ListInventorySupplyByNextTokenResponse", propOrder={
    "listInventorySupplyByNextTokenResult",
    "responseMetadata"
})
@XmlRootElement(name = "ListInventorySupplyByNextTokenResponse")
public class ListInventorySupplyByNextTokenResponse extends AbstractMwsObject implements MWSResponse {

    @XmlElement(name="ListInventorySupplyByNextTokenResult")
    private ListInventorySupplyByNextTokenResult listInventorySupplyByNextTokenResult;

    @XmlElement(name="ResponseMetadata")
    private ResponseMetadata responseMetadata;

    @XmlTransient
    private ResponseHeaderMetadata responseHeaderMetadata;

    /**
     * Get the value of ListInventorySupplyByNextTokenResult.
     *
     * @return The value of ListInventorySupplyByNextTokenResult.
     */
    public ListInventorySupplyByNextTokenResult getListInventorySupplyByNextTokenResult() {
        return this.listInventorySupplyByNextTokenResult;
    }

    /**
     * Set the value of ListInventorySupplyByNextTokenResult.
     *
     * @param listInventorySupplyByNextTokenResult
     *            The new value to set.
     */
    public void setListInventorySupplyByNextTokenResult(ListInventorySupplyByNextTokenResult listInventorySupplyByNextTokenResult) {
        this.listInventorySupplyByNextTokenResult = listInventorySupplyByNextTokenResult;
    }

    /**
     * Check to see if ListInventorySupplyByNextTokenResult is set.
     *
     * @return true if ListInventorySupplyByNextTokenResult is set.
     */
    public boolean isSetListInventorySupplyByNextTokenResult() {
        return this.listInventorySupplyByNextTokenResult != null;
    }

    /**
     * Set the value of ListInventorySupplyByNextTokenResult, return this.
     *
     * @param listInventorySupplyByNextTokenResult
     *             The new value to set.
     *
     * @return This instance.
     */
    public ListInventorySupplyByNextTokenResponse withListInventorySupplyByNextTokenResult(ListInventorySupplyByNextTokenResult listInventorySupplyByNextTokenResult) {
        this.listInventorySupplyByNextTokenResult = listInventorySupplyByNextTokenResult;
        return this;
    }

    /**
     * Get the value of ResponseMetadata.
     *
     * @return The value of ResponseMetadata.
     */
    public ResponseMetadata getResponseMetadata() {
        return this.responseMetadata;
    }

    /**
     * Set the value of ResponseMetadata.
     *
     * @param responseMetadata
     *            The new value to set.
     */
    public void setResponseMetadata(ResponseMetadata responseMetadata) {
        this.responseMetadata = responseMetadata;
    }

    /**
     * Check to see if ResponseMetadata is set.
     *
     * @return true if ResponseMetadata is set.
     */
    public boolean isSetResponseMetadata() {
        return this.responseMetadata != null;
    }

    /**
     * Set the value of ResponseMetadata, return this.
     *
     * @param responseMetadata
     *             The new value to set.
     *
     * @return This instance.
     */
    public ListInventorySupplyByNextTokenResponse withResponseMetadata(ResponseMetadata responseMetadata) {
        this.responseMetadata = responseMetadata;
        return this;
    }

    /**
     * Get the value of ResponseHeaderMetadata.
     *
     * @return The value of ResponseHeaderMetadata.
     */
    public ResponseHeaderMetadata getResponseHeaderMetadata() {
        return this.responseHeaderMetadata;
    }

    /**
     * Set the value of ResponseHeaderMetadata.
     *
     * @param responseHeaderMetadata
     *            The new value to set.
     */
    public void setResponseHeaderMetadata(ResponseHeaderMetadata responseHeaderMetadata) {
        this.responseHeaderMetadata = responseHeaderMetadata;
    }

    /**
     * Check to see if ResponseHeaderMetadata is set.
     *
     * @return true if ResponseHeaderMetadata is set.
     */
    public boolean isSetResponseHeaderMetadata() {
        return this.responseHeaderMetadata != null;
    }

    /**
     * Set the value of ResponseHeaderMetadata, return this.
     *
     * @param responseHeaderMetadata
     *             The new value to set.
     *
     * @return This instance.
     */
    public ListInventorySupplyByNextTokenResponse withResponseHeaderMetadata(ResponseHeaderMetadata responseHeaderMetadata) {
        this.responseHeaderMetadata = responseHeaderMetadata;
        return this;
    }

    /**
     * Read members from a MwsReader.
     *
     * @param r
     *      The reader to read from.
     */
    @Override
    public void readFragmentFrom(MwsReader r) {
        this.listInventorySupplyByNextTokenResult = r.read("ListInventorySupplyByNextTokenResult", ListInventorySupplyByNextTokenResult.class);
        this.responseMetadata = r.read("ResponseMetadata", ResponseMetadata.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("ListInventorySupplyByNextTokenResult", this.listInventorySupplyByNextTokenResult);
        w.write("ResponseMetadata", this.responseMetadata);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("http://mws.amazonaws.com/FulfillmentInventory/2010-10-01/", "ListInventorySupplyByNextTokenResponse",this);
    }

    /** Value constructor. */
    public ListInventorySupplyByNextTokenResponse(ListInventorySupplyByNextTokenResult listInventorySupplyByNextTokenResult,ResponseMetadata responseMetadata) {
        this.listInventorySupplyByNextTokenResult = listInventorySupplyByNextTokenResult;
        this.responseMetadata = responseMetadata;
    }


    /** Default constructor. */
    public ListInventorySupplyByNextTokenResponse() {
        super();
    }

}
