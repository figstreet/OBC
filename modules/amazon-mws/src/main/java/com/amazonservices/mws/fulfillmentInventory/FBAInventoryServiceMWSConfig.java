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
 * FBA Inventory Service MWS
 * API Version: 2010-10-01
 * Library Version: 2014-09-30
 * Generated: Mon Mar 21 09:01:27 PDT 2016
 */
package com.amazonservices.mws.fulfillmentInventory;

import java.net.URI;

import com.amazonservices.mws.client.MwsConnection;
import com.amazonservices.mws.client.MwsUtl;

/**
 * Configuration for a connection.
 */
public class FBAInventoryServiceMWSConfig {

    /** The service path for this API. */
    static final String DEFAULT_SERVICE_PATH = "FulfillmentInventory/2010-10-01";

    /** The service path. */
    private String servicePath;

    /** A connection used to hold configuration. */
    private final MwsConnection cc = new MwsConnection();

    /**
     * Get a clone of the configured connection.
     *
     * @return A clone of the configured connection.
     */
    MwsConnection copyConnection() {
        return this.cc.clone();
    }

    /**
     * Gets MaxConnections property
     *
     * @return Max number of http connections
     */
    public int getMaxConnections() {
        return this.cc.getMaxConnections();
    }

    public void setMaxConnections(int maxConnections) {
        this.cc.setMaxConnections(maxConnections);
    }

    public FBAInventoryServiceMWSConfig withMaxConnections(int maxConnections) {
        this.cc.setMaxConnections(maxConnections);
        return this;
    }

    public boolean isSetMaxConnections() {
        return this.cc.getMaxConnections() > 0;
    }


    /**
     * Gets MaxErrorRetry property
     *
     * @return Max number of retries on 500 errors
     */
    public int getMaxErrorRetry() {
        return this.cc.getMaxErrorRetry();
    }

    public void setMaxErrorRetry(int maxErrorRetry) {
        this.cc.setMaxErrorRetry(maxErrorRetry);
    }

    public FBAInventoryServiceMWSConfig withMaxErrorRetry(int maxErrorRetry) {
        this.cc.setMaxErrorRetry(maxErrorRetry);
        return this;
    }

    public boolean isSetMaxErrorRetry() {
        return this.cc.getMaxErrorRetry() > 0;
    }

    /**
     * Gets ProxyHost property
     *
     * @return Proxy Host for connection
     */
    public String getProxyHost() {
        return this.cc.getProxyHost();
    }

    public void setProxyHost(String proxyHost) {
        this.cc.setProxyHost(proxyHost);
    }

    public FBAInventoryServiceMWSConfig withProxyHost(String proxyHost) {
        this.cc.setProxyHost(proxyHost);
        return this;
    }

    public boolean isSetProxyHost() {
        return this.cc.getProxyHost() != null;
    }

    /**
     * Gets ProxyPassword property
     *
     * @return Proxy Password
     */
    public String getProxyPassword() {
        return this.cc.getProxyPassword();
    }

    public void setProxyPassword(String proxyPassword) {
        this.cc.setProxyPassword(proxyPassword);
    }

    public FBAInventoryServiceMWSConfig withProxyPassword(String proxyPassword) {
        this.cc.setProxyPassword(proxyPassword);
        return this;
    }

    public boolean isSetProxyPassword() {
        return this.cc.getProxyPassword() != null;
    }

    /**
     * Gets ProxyPort property
     *
     * @return Proxy Port for connection
     */
    public int getProxyPort() {
        return this.cc.getProxyPort();
    }

     public void setProxyPort(int proxyPort) {
        this.cc.setProxyPort(proxyPort);
    }

    public FBAInventoryServiceMWSConfig withProxyPort(int proxyPort) {
        this.cc.setProxyPort(proxyPort);
        return this;
    }

    public boolean isSetProxyPort() {
        return this.cc.getProxyPort() != -1;
    }

    /**
     * Gets ProxyUsername property
     *
     * @return Proxy Username
     */
    public String getProxyUsername() {
        return this.cc.getProxyUsername();
    }

    public void setProxyUsername(String proxyUsername) {
        this.cc.setProxyUsername(proxyUsername);
    }

    public FBAInventoryServiceMWSConfig withProxyUsername(String proxyUsername) {
        this.cc.setProxyUsername(proxyUsername);
        return this;
    }

    public boolean isSetProxyUsername() {
        return this.cc.getProxyUsername() != null;
    }

    public String getServiceVersion() {
        return "2010-10-01";
    }

	/**
	 * Gets the SignatureVersion property
	 *
	 * @return Signature Version
	 */
    public String getSignatureVersion() {
        return this.cc.getSignatureVersion();
    }

    public void setSignatureVersion(String signatureVersion) {
        this.cc.setSignatureVersion(signatureVersion);
    }

    public FBAInventoryServiceMWSConfig withSignatureVersion(String signatureVersion) {
        this.cc.setSignatureVersion(signatureVersion);
        return this;
    }

    public boolean isSetSignatureVersion() {
        return true;
    }

    /**
     * Gets the SignatureMethod property
     *
     * @return Signature Method
     */
    public String getSignatureMethod() {
        return this.cc.getSignatureMethod();
    }

    public void setSignatureMethod(String signatureMethod) {
        this.cc.setSignatureMethod(signatureMethod);
    }

    public FBAInventoryServiceMWSConfig withSignatureMethod(String signatureMethod) {
        this.cc.setSignatureMethod(signatureMethod);
        return this;
    }

    public boolean isSetSignatureMethod() {
        return true;
    }

    public String getUserAgent() {
        return this.cc.getUserAgent();
    }

    public void setUserAgent(String applicationName, String applicationVersion, String programmingLanguage,
            String... additionalNameValuePairs) {
        this.cc.setUserAgent(applicationName,applicationVersion,programmingLanguage,additionalNameValuePairs);
    }

    public FBAInventoryServiceMWSConfig withUserAgent(String applicationName, String applicationVersion,
        String programmingLanguage, String... additionalNameValuePairs) {
        this.cc.setUserAgent(applicationName, applicationVersion, programmingLanguage, additionalNameValuePairs);
        return this;
    }

    public boolean isSetUserAgent() {
        return this.cc.getUserAgent() != null;
    }

    /**
     * Gets the path appended after the endpoint
     *
     * @returns the path appended after the endpoint
     */
    String getServicePath() {
        return this.servicePath;
    }

    public String getServiceURL() {
        return this.cc.getEndpoint().toString() + "/" + this.servicePath;
    }

    /**
     * Set the service URL to make requests against.
     *
     * This can either be just the host name or can include the full path to the service.
     *
     * @param serviceUrl URL to make requests against
     */
    public void setServiceURL(String serviceUrl) {
        try {
            URI fullURI = URI.create(serviceUrl);
            URI partialURI = new URI(fullURI.getScheme(), null, fullURI.getHost(),
                fullURI.getPort(), null, null, null);
            this.cc.setEndpoint(partialURI);
            String path = fullURI.getPath();
            if (path != null) {
                path = path.substring(path.startsWith("/") ? 1 : 0);
                path = path.substring(0, path.length() - (path.endsWith("/") ? 1 : 0));
            }
            if (path == null || path.isEmpty()) {
                this.servicePath = DEFAULT_SERVICE_PATH;
            } else {
                this.servicePath = path;
            }
        } catch (Exception e) {
            throw MwsUtl.wrap(e);
        }
    }

    public FBAInventoryServiceMWSConfig withServiceURL(String aserviceUrl) {
        this.setServiceURL(aserviceUrl);
        return this;
    }

    public boolean isSetServiceURL() {
        return this.servicePath != null;
    }

    /**
     * Get the configured max async queue size.
     * <p>
     * This is the max number of requests to queue before executing
     * requests on the calling thread.
     *
     * @return The max async queue size property.
     */
    public int getMaxAsyncQueueSize() {
        return this.cc.getMaxAsyncQueueSize();
    }

    public void setMaxAsyncQueueSize(int size) {
        this.cc.setMaxAsyncQueueSize(size);
    }

    public FBAInventoryServiceMWSConfig withMaxAsyncQueueSize(int size) {
        this.cc.setMaxAsyncQueueSize(size);
        return this;
    }

    public boolean isSetMaxAsyncQueueSize() {
       return this.cc.getMaxAsyncQueueSize()>0;
    }

    /**
     * Get the configured max async thread count.
     * <p>
     * This is the number of async threads to process requests from the queue.
     *
     * @return The max async thread count.
     */
    public int getMaxAsyncThreads() {
        return this.cc.getMaxAsyncThreads();
    }

    public void setMaxAsyncThreads(int threads) {
        this.cc.setMaxAsyncThreads(threads);
    }

    public FBAInventoryServiceMWSConfig withMaxAsyncThreads(int threads) {
        this.cc.setMaxAsyncThreads(threads);
        return this;
    }

    public boolean isSetMaxAsyncThreads() {
       return this.cc.getMaxAsyncThreads()>0;
    }

    /**
     * Sets the value of a request header to be included on every request
     *
     * @param name the name of the header to set
     * @param value value to send with header
     */
    public void includeRequestHeader(String name, String value) {
        this.cc.includeRequestHeader(name, value);
    }

    /**
     * Gets the currently set value of a request header
     *
     * @param name the name of the header to get
     * @return value of specified header, or null if not defined
     */
    public String getRequestHeader(String name) {
        return this.cc.getRequestHeader(name);
    }

    /**
     * Sets the value of a request header to be included on every request
     *
     * @param name the name of the header to set
     * @param value value to send with header
     * @return the current config object
     */
    public FBAInventoryServiceMWSConfig withRequestHeader(String name, String value) {
        this.cc.includeRequestHeader(name, value);
        return this;
    }

    /**
     * Checks if a request header is set
     *
     * @param name the name of the header to check
     * @return true, if the header is set
     */
    public boolean isSetRequestHeader(String name) {
        return this.cc.getRequestHeader(name) != null;
    }
}
