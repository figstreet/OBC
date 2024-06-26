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
 * Get Service Status Result
 * API Version: 2010-10-01
 * Library Version: 2014-09-30
 * Generated: Mon Mar 21 09:01:27 PDT 2016
 */
package com.amazonservices.mws.fulfillmentInventory.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.client.AbstractMwsObject;
import com.amazonservices.mws.client.MwsReader;
import com.amazonservices.mws.client.MwsWriter;

/**
 * GetServiceStatusResult complex type.
 *
 * XML schema:
 *
 * <pre>
 * &lt;complexType name="GetServiceStatusResult"&gt;
 *    &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *          &lt;sequence&gt;
 *             &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *             &lt;element name="Timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *          &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *    &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="GetServiceStatusResult", propOrder={
    "status",
    "timestamp"
})
@XmlRootElement(name = "GetServiceStatusResult")
public class GetServiceStatusResult extends AbstractMwsObject {

    @XmlElement(name="Status")
    private String status;

    @XmlElement(name="Timestamp")
    private XMLGregorianCalendar timestamp;

    /**
     * Get the value of Status.
     *
     * @return The value of Status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Set the value of Status.
     *
     * @param status
     *            The new value to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Check to see if Status is set.
     *
     * @return true if Status is set.
     */
    public boolean isSetStatus() {
        return this.status != null;
    }

    /**
     * Set the value of Status, return this.
     *
     * @param status
     *             The new value to set.
     *
     * @return This instance.
     */
    public GetServiceStatusResult withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get the value of Timestamp.
     *
     * @return The value of Timestamp.
     */
    public XMLGregorianCalendar getTimestamp() {
        return this.timestamp;
    }

    /**
     * Set the value of Timestamp.
     *
     * @param timestamp
     *            The new value to set.
     */
    public void setTimestamp(XMLGregorianCalendar timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Check to see if Timestamp is set.
     *
     * @return true if Timestamp is set.
     */
    public boolean isSetTimestamp() {
        return this.timestamp != null;
    }

    /**
     * Set the value of Timestamp, return this.
     *
     * @param timestamp
     *             The new value to set.
     *
     * @return This instance.
     */
    public GetServiceStatusResult withTimestamp(XMLGregorianCalendar timestamp) {
        this.timestamp = timestamp;
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
        this.status = r.read("Status", String.class);
        this.timestamp = r.read("Timestamp", XMLGregorianCalendar.class);
    }

    /**
     * Write members to a MwsWriter.
     *
     * @param w
     *      The writer to write to.
     */
    @Override
    public void writeFragmentTo(MwsWriter w) {
        w.write("Status", this.status);
        w.write("Timestamp", this.timestamp);
    }

    /**
     * Write tag, xmlns and members to a MwsWriter.
     *
     * @param w
     *         The Writer to write to.
     */
    @Override
    public void writeTo(MwsWriter w) {
        w.write("http://mws.amazonaws.com/FulfillmentInventory/2010-10-01/", "GetServiceStatusResult",this);
    }

    /** Value constructor. */
    public GetServiceStatusResult(String status,XMLGregorianCalendar timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }


    /** Default constructor. */
    public GetServiceStatusResult() {
        super();
    }

}
