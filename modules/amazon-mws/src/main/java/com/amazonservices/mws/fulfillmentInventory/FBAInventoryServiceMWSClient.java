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

import com.amazonservices.mws.client.MwsConnection;
import com.amazonservices.mws.client.MwsException;
import com.amazonservices.mws.client.MwsObject;
import com.amazonservices.mws.client.MwsRequestType;
import com.amazonservices.mws.client.MwsResponseHeaderMetadata;
import com.amazonservices.mws.client.MwsUtl;
import com.amazonservices.mws.fulfillmentInventory.model.GetServiceStatusRequest;
import com.amazonservices.mws.fulfillmentInventory.model.GetServiceStatusResponse;
import com.amazonservices.mws.fulfillmentInventory.model.ListInventorySupplyByNextTokenRequest;
import com.amazonservices.mws.fulfillmentInventory.model.ListInventorySupplyByNextTokenResponse;
import com.amazonservices.mws.fulfillmentInventory.model.ListInventorySupplyRequest;
import com.amazonservices.mws.fulfillmentInventory.model.ListInventorySupplyResponse;
import com.amazonservices.mws.fulfillmentInventory.model.MWSResponse;
import com.amazonservices.mws.fulfillmentInventory.model.ResponseHeaderMetadata;

public class FBAInventoryServiceMWSClient implements FBAInventoryServiceMWS {

    private static final String libraryName = "FBAInventoryServiceMWS";

    private static final String libraryVersion = "2014-09-30";

    protected String servicePath;

    protected final MwsConnection connection;

    public FBAInventoryServiceMWSClient(
            String accessKey,
            String secretKey,
            String applicationName,
            String applicationVersion,
            FBAInventoryServiceMWSConfig config) {
        this.connection = config.copyConnection();
        this.connection.setAwsAccessKeyId(accessKey);
        this.connection.setAwsSecretKeyId(secretKey);
        this.connection.setApplicationName(applicationName);
        this.connection.setApplicationVersion(applicationVersion);
        this.connection.setLibraryVersion(libraryVersion);
        this.servicePath = config.getServicePath();
    }

    public FBAInventoryServiceMWSClient(
            String accessKey,
            String secretKey,
            FBAInventoryServiceMWSConfig config) {
        this(accessKey, secretKey, libraryName, libraryVersion, config);
    }

    public FBAInventoryServiceMWSClient(
            String accessKey,
            String secretKey,
            String applicationName,
            String applicationVersion) {
        this(accessKey, secretKey, applicationName,
                applicationVersion, new FBAInventoryServiceMWSConfig());
    }

    public GetServiceStatusResponse getServiceStatus(GetServiceStatusRequest request) {
        return this.connection.call(
            new RequestType("GetServiceStatus", GetServiceStatusResponse.class, this.servicePath),
            request);
    }

    public ListInventorySupplyResponse listInventorySupply(ListInventorySupplyRequest request) {
        return this.connection.call(
            new RequestType("ListInventorySupply", ListInventorySupplyResponse.class, this.servicePath),
            request);
    }

    public ListInventorySupplyByNextTokenResponse listInventorySupplyByNextToken(ListInventorySupplyByNextTokenRequest request) {
        return this.connection.call(
            new RequestType("ListInventorySupplyByNextToken", ListInventorySupplyByNextTokenResponse.class, this.servicePath),
            request);
    }

    public static String quoteAppName(String s) {
        return MwsUtl.escapeAppName(s);
    }

    public static String quoteAppVersion(String s) {
        return MwsUtl.escapeAppVersion(s);
    }

    public static String quoteAttributeName(String s) {
        return MwsUtl.escapeAttributeName(s);
    }

    public static String quoteAttributeValue(String s) {
        return MwsUtl.escapeAttributeValue(s);
    }

    protected static class RequestType implements MwsRequestType {

        private final String operationName;
        private final Class<? extends MWSResponse> responseClass;
        private final String servicePath;

        public RequestType(String operationName, Class<? extends MWSResponse> responseClass, String servicePath) {
            this.operationName = operationName;
            this.responseClass = responseClass;
            this.servicePath = servicePath;
        }

        @Override
        public String getServicePath() {
            return this.servicePath;
        }

        @Override
        public String getOperationName() {
            return this.operationName;
        }

        @Override
        public Class<? extends MwsObject> getResponseClass() {
            return this.responseClass;
        }

        @Override
        public MwsException wrapException(Throwable cause) {
            return new FBAInventoryServiceMWSException(cause);
        }

        @Override
        public void setRHMD(MwsObject response, MwsResponseHeaderMetadata rhmd) {
            ((MWSResponse)response).setResponseHeaderMetadata(new ResponseHeaderMetadata(rhmd));
        }
    }

}
