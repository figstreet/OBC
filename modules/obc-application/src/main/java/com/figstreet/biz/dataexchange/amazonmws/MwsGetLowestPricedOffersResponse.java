package com.figstreet.biz.dataexchange.amazonmws;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForASINResponse;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForASINResult;

public class MwsGetLowestPricedOffersResponse extends MwsMethodResponse
{
	private GetLowestPricedOffersForASINResponse fResponse;
	private MwsPricing fMwsPricing;

	public MwsGetLowestPricedOffersResponse(GetLowestPricedOffersForASINResponse pResponse)
	{
		super(pResponse);
		this.fResponse = pResponse;
		GetLowestPricedOffersForASINResult result = pResponse.getGetLowestPricedOffersForASINResult();
		if (result != null)
			this.fMwsPricing = new MwsPricing(result);
	}

	public MwsGetLowestPricedOffersResponse(MarketplaceWebServiceProductsException pException)
	{
		super(pException);
	}

	@Override
	public boolean isError()
	{
		return this.fMwsPricing == null;
	}

	public MwsPricing getMwsPricing()
	{
		return this.fMwsPricing;
	}
}
