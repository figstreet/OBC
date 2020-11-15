package com.figstreet.biz.dataexchange.amazonmws;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;
import com.figstreet.core.DateUtil;

public class MwsRequestThrottledResponse extends MwsMethodResponse
{
	public MwsRequestThrottledResponse(String pMethodName, ResponseHeaderMetadata pHeaderMetadata)
	{
		super(pHeaderMetadata, String.format("Cannot request %s until %s", pMethodName,
				DateUtil.formatBoth(pHeaderMetadata.getQuotaResetsAt())));
	}

	public MwsRequestThrottledResponse(MarketplaceWebServiceProductsException pException)
	{
		super(pException);
	}

	@Override
	public boolean isError()
	{
		return true;
	}

}
