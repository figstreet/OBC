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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.client.MwsUtl;
import com.amazonservices.mws.fulfillmentInventory.FBAInventoryServiceMWSAsync;
import com.amazonservices.mws.fulfillmentInventory.FBAInventoryServiceMWSAsyncClient;
import com.amazonservices.mws.fulfillmentInventory.FBAInventoryServiceMWSException;
import com.amazonservices.mws.fulfillmentInventory.model.ListInventorySupplyRequest;
import com.amazonservices.mws.fulfillmentInventory.model.ListInventorySupplyResponse;
import com.amazonservices.mws.fulfillmentInventory.model.ResponseHeaderMetadata;
import com.amazonservices.mws.fulfillmentInventory.model.SellerSkuList;

/** Sample async call for ListInventorySupply. */
public class ListInventorySupplyAsyncSample {

    /**
     * Call the service, log response and exceptions.
     *
     * @param client
     * @param request
     *
     * @return The response.
     */
    public static List<Object> invokeListInventorySupply(
            FBAInventoryServiceMWSAsync client,
            List<ListInventorySupplyRequest> requestList) {
        // Call the service async.
        List<Future<ListInventorySupplyResponse>> futureList =
            new ArrayList<Future<ListInventorySupplyResponse>>();
        for (ListInventorySupplyRequest request : requestList) {
            Future<ListInventorySupplyResponse> future =
                client.listInventorySupplyAsync(request);
            futureList.add(future);
        }
        List<Object> responseList = new ArrayList<Object>();
        for (Future<ListInventorySupplyResponse> future : futureList) {
            Object xresponse;
            try {
                ListInventorySupplyResponse response = future.get();
                ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
                // We recommend logging every the request id and timestamp of every call.
                System.out.println("Response:");
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
                String responseXml = response.toXML();
                System.out.println(responseXml);
                xresponse = response;
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof FBAInventoryServiceMWSException) {
                    // Exception properties are important for diagnostics.
                    FBAInventoryServiceMWSException ex =
                        (FBAInventoryServiceMWSException)cause;
                    ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
                    System.out.println("Service Exception:");
                    System.out.println("RequestId: "+rhmd.getRequestId());
                    System.out.println("Timestamp: "+rhmd.getTimestamp());
                    System.out.println("Message: "+ex.getMessage());
                    System.out.println("StatusCode: "+ex.getStatusCode());
                    System.out.println("ErrorCode: "+ex.getErrorCode());
                    System.out.println("ErrorType: "+ex.getErrorType());
                    xresponse = ex;
                } else {
                    xresponse = cause;
                }
            } catch (Exception e) {
                xresponse = e;
            }
            responseList.add(xresponse);
        }
        return responseList;
    }

    /**
     *  Command line entry point.
     */
    public static void main(String[] args) {

        // Get a client connection.
        FBAInventoryServiceMWSAsyncClient client = FBAInventoryServiceMWSSampleConfig.getAsyncClient();

        // Create a request list.
        List<ListInventorySupplyRequest> requestList = new ArrayList<ListInventorySupplyRequest>();
        ListInventorySupplyRequest request = new ListInventorySupplyRequest();
        String sellerId = "example";
        request.setSellerId(sellerId);
        String mwsAuthToken = "example";
        request.setMWSAuthToken(mwsAuthToken);
        String marketplace = "example";
        request.setMarketplace(marketplace);
        String marketplaceId = "example";
        request.setMarketplaceId(marketplaceId);
        SellerSkuList sellerSkus = new SellerSkuList();
        request.setSellerSkus(sellerSkus);
        XMLGregorianCalendar queryStartDateTime = MwsUtl.getDTF().newXMLGregorianCalendar();
        request.setQueryStartDateTime(queryStartDateTime);
        String responseGroup = "example";
        request.setResponseGroup(responseGroup);
        requestList.add(request);

        // Make the calls.
        ListInventorySupplyAsyncSample.invokeListInventorySupply(client, requestList);

    }

}
