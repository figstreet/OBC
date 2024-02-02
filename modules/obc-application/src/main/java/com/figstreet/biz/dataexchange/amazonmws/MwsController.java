package com.figstreet.biz.dataexchange.amazonmws;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForASINRequest;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForASINResponse;
import com.amazonservices.mws.products.model.GetMatchingProductForIdRequest;
import com.amazonservices.mws.products.model.GetMatchingProductForIdResponse;
import com.amazonservices.mws.products.model.IdListType;
import com.amazonservices.mws.products.model.ListMatchingProductsRequest;
import com.amazonservices.mws.products.model.ListMatchingProductsResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.data.common.ProductCondition;

/*
 * This class implement methods to execute Amazon MWS functions
 *
 * It will also keep track of the request limits imposed by Amazon
 */

public class MwsController
{
	private ConcurrentHashMap<String, ResponseHeaderMetadata> fPriorResponseHeaderMetadata = new ConcurrentHashMap<>(
			10);

	private static final Object LOCK = new Object();
	private MarketplaceWebServiceProductsAsyncClient fProductsClient = null;

	public MwsController()
	{

	}

	public MarketplaceWebServiceProductsClient getProductsClient()
	{
		if (this.fProductsClient != null)
			return this.fProductsClient;

		synchronized (LOCK)
		{
			if (this.fProductsClient == null)
			{
				MarketplaceWebServiceProductsConfig mwspConfig = new MarketplaceWebServiceProductsConfig();
				MarketplaceWebServiceConfig mwsConfig = new MarketplaceWebServiceConfig();
				mwspConfig.setServiceURL(mwsConfig.getServiceURL());
				// Set other client connection configurations here.
				this.fProductsClient = new MarketplaceWebServiceProductsAsyncClient(mwsConfig.getAwsAccessKey(),
						mwsConfig.getAwsSecretKey(), mwsConfig.getAppName(), mwsConfig.getAppVersion(), mwspConfig,
						null);
			}
		}

		return this.fProductsClient;
	}

	public GetMatchingProductForIdRequest createGetMatchingProductsForIdRequest(MwsIdType pIdType,
			List<String> pKeyList)
	{
		if (pKeyList == null || pKeyList.isEmpty())
			throw new IllegalArgumentException("pKeyList must contain a value");
		if (pIdType == null)
			throw new IllegalArgumentException("pIdType must be provided");

		MarketplaceWebServiceConfig mwsConfig = new MarketplaceWebServiceConfig();
		if (pKeyList.size() > mwsConfig.getMaxGetMatchingProductForId())
			throw new IllegalArgumentException(
					"pKeyList size must be less than " + (mwsConfig.getMaxGetMatchingProductForId() + 1));

		SellerAccountConfig sellerAccountConfig = new SellerAccountConfig();

		IdListType mwsIdList = new IdListType();
		mwsIdList.setId(pKeyList);

		// Create a request.
		GetMatchingProductForIdRequest request = new GetMatchingProductForIdRequest(sellerAccountConfig.getSellerId(),
				sellerAccountConfig.getMarketplaceId(), pIdType.toString(), mwsIdList);
		request.setMWSAuthToken(sellerAccountConfig.getMwsAuthToken());
		return request;
	}

	public synchronized MwsMethodResponse getMatchingProductsForId(GetMatchingProductForIdRequest pRequest)
	{
		if (pRequest == null)
			throw new IllegalArgumentException("GetMatchingProductForIdRequest cannot be null");

		if (pRequest.getIdList() == null || pRequest.getIdList().getId() == null
				|| pRequest.getIdList().getId().isEmpty())
			throw new IllegalArgumentException("GetMatchingProductForIdRequest IdList must contain a value");

		String methodName = "getMatchingProductForId";
		ResponseHeaderMetadata rhmd = this.fPriorResponseHeaderMetadata.get(methodName);
		if (rhmd != null)
		{
			Double remaining = rhmd.getQuotaRemaining();
			Date resetAt = rhmd.getQuotaResetsAt();
			if (resetAt == null)
				resetAt = DateUtil.now();
			if (remaining != null && remaining < pRequest.getIdList().getId().size() && resetAt.after(DateUtil.now()))
			{
				return new MwsRequestThrottledResponse(methodName, rhmd);
			}
		}

		try
		{
			MarketplaceWebServiceProductsClient client = this.getProductsClient();
			GetMatchingProductForIdResponse mwsResponse = client.getMatchingProductForId(pRequest);
			this.fPriorResponseHeaderMetadata.put(methodName, mwsResponse.getResponseHeaderMetadata());
			return new MwsGetProductForIdResponse(mwsResponse);
		}
		catch (MarketplaceWebServiceProductsException e)
		{
			return new MwsGetProductForIdResponse(e);
		}
	}

	public ListMatchingProductsRequest createListMatchingProductsRequest(String pQuery)
	{
		if (CompareUtil.isEmpty(pQuery))
			throw new IllegalArgumentException("pQuery must contain a value");

		SellerAccountConfig sellerAccountConfig = new SellerAccountConfig();

		// Create a request.
		ListMatchingProductsRequest request = new ListMatchingProductsRequest(sellerAccountConfig.getSellerId(),
				sellerAccountConfig.getMarketplaceId(), pQuery);
		request.setMWSAuthToken(sellerAccountConfig.getMwsAuthToken());
		return request;
	}

	public synchronized MwsMethodResponse getListMatchingProducts(ListMatchingProductsRequest pRequest,
			MwsIdType pIdType, String pIdentifier)
	{
		if (pRequest == null)
			throw new IllegalArgumentException("ListMatchingProductsRequest cannot be null");

		String methodName = "listMatchingProducts";
		ResponseHeaderMetadata rhmd = this.fPriorResponseHeaderMetadata.get(methodName);
		if (rhmd != null)
		{
			Double remaining = rhmd.getQuotaRemaining();
			Date resetAt = rhmd.getQuotaResetsAt();
			if (resetAt == null)
				resetAt = DateUtil.now();
			if (remaining != null && remaining < 1 && resetAt.after(DateUtil.now()))
			{
				return new MwsRequestThrottledResponse(methodName, rhmd);
			}
		}

		try
		{
			MarketplaceWebServiceProductsClient client = this.getProductsClient();
			ListMatchingProductsResponse mwsResponse = client.listMatchingProducts(pRequest);
			this.fPriorResponseHeaderMetadata.put(methodName, mwsResponse.getResponseHeaderMetadata());
			return new MwsListMatchingProductsResponse(mwsResponse, pIdType, pIdentifier);
		}
		catch (MarketplaceWebServiceProductsException e)
		{
			return new MwsGetProductForIdResponse(e);
		}
	}

	public GetLowestPricedOffersForASINRequest createGetLowestPricedOffersForASINRequest(MwsProduct pMwsProduct,
			ProductCondition pProductCondition)
	{
		if (pMwsProduct == null)
			throw new IllegalArgumentException("pMwsProduct must be specified");
		if (CompareUtil.isEmpty(pMwsProduct.getAsin()))
			throw new IllegalArgumentException("pMwsProduct Asin must contain a value");
		if (pProductCondition == null)
			throw new IllegalArgumentException("pProductCondition must be specified");

		SellerAccountConfig sellerAccountConfig = new SellerAccountConfig();

		// Create a request.
		GetLowestPricedOffersForASINRequest request = new GetLowestPricedOffersForASINRequest(
				sellerAccountConfig.getSellerId(), sellerAccountConfig.getMarketplaceId(), pMwsProduct.getAsin(),
				pProductCondition.toString());
		request.setMWSAuthToken(sellerAccountConfig.getMwsAuthToken());
		return request;
	}

	public synchronized MwsMethodResponse getLowestPricedOffersForASIN(GetLowestPricedOffersForASINRequest pRequest)
	{
		if (pRequest == null)
			throw new IllegalArgumentException("pRequest must be specified");

		String methodName = "getLowestPricedOffersForASIN";
		ResponseHeaderMetadata rhmd = this.fPriorResponseHeaderMetadata.get(methodName);
		if (rhmd != null)
		{
			Double remaining = rhmd.getQuotaRemaining();
			Date resetAt = rhmd.getQuotaResetsAt();
			if (resetAt == null)
				resetAt = DateUtil.now();
			if (remaining != null && remaining < 1 && resetAt.after(DateUtil.now()))
			{
				return new MwsRequestThrottledResponse(methodName, rhmd);
			}
		}

		try
		{
			MarketplaceWebServiceProductsClient client = this.getProductsClient();
			GetLowestPricedOffersForASINResponse mwsResponse = client.getLowestPricedOffersForASIN(pRequest);
			this.fPriorResponseHeaderMetadata.put(methodName, mwsResponse.getResponseHeaderMetadata());
			return new MwsGetLowestPricedOffersResponse(mwsResponse);
		}
		catch (MarketplaceWebServiceProductsException e)
		{
			return new MwsGetProductForIdResponse(e);
		}
	}

}
