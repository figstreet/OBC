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
package com.amazonservices.mws.fulfillmentInventory.samples;

import com.amazonservices.mws.fulfillmentInventory.FBAInventoryServiceMWS;
import com.amazonservices.mws.fulfillmentInventory.FBAInventoryServiceMWSClient;
import com.amazonservices.mws.fulfillmentInventory.FBAInventoryServiceMWSException;
import com.amazonservices.mws.fulfillmentInventory.model.GetServiceStatusRequest;
import com.amazonservices.mws.fulfillmentInventory.model.GetServiceStatusResponse;
import com.amazonservices.mws.fulfillmentInventory.model.ResponseHeaderMetadata;


/** Sample call for GetServiceStatus. */
public class GetServiceStatusSample {

    /**
     * Call the service, log response and exceptions.
     *
     * @param client
     * @param request
     *
     * @return The response.
     */
    public static GetServiceStatusResponse invokeGetServiceStatus(
            FBAInventoryServiceMWS client,
            GetServiceStatusRequest request) {
        try {
            // Call the service.
            GetServiceStatusResponse response = client.getServiceStatus(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            System.out.println("Response:");
            System.out.println("RequestId: "+rhmd.getRequestId());
            System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            System.out.println(responseXml);
            return response;
        } catch (FBAInventoryServiceMWSException ex) {
            // Exception properties are important for diagnostics.
            System.out.println("Service Exception:");
            ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
            if(rhmd != null) {
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
            }
            System.out.println("Message: "+ex.getMessage());
            System.out.println("StatusCode: "+ex.getStatusCode());
            System.out.println("ErrorCode: "+ex.getErrorCode());
            System.out.println("ErrorType: "+ex.getErrorType());
            throw ex;
        }
    }

    /**
     *  Command line entry point.
     */
    public static void main(String[] args) {

        // Get a client connection.
        // Make sure you've set the variables in FBAInventoryServiceMWSSampleConfig.
        FBAInventoryServiceMWSClient client = FBAInventoryServiceMWSSampleConfig.getClient();

        // Create a request.
        GetServiceStatusRequest request = new GetServiceStatusRequest();
        String sellerId = "";
        request.setSellerId(sellerId);
        String mwsAuthToken = "";
        request.setMWSAuthToken(mwsAuthToken);
        String marketplace = "ATVPDKIKX0DER";
        request.setMarketplace(marketplace);

        // Make the call.
        GetServiceStatusSample.invokeGetServiceStatus(client, request);

    }

}
