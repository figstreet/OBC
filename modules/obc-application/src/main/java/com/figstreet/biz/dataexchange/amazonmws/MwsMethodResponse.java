package com.figstreet.biz.dataexchange.amazonmws;

import java.util.Date;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.MWSResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;

public abstract class MwsMethodResponse
{
	protected ResponseHeaderMetadata fHeaderMetadata;
	protected String fErrorMessage;

	public MwsMethodResponse(MWSResponse pResponse)
	{
		this.fHeaderMetadata = pResponse.getResponseHeaderMetadata();
	}

	public MwsMethodResponse(MarketplaceWebServiceProductsException pException)
	{
		this.fHeaderMetadata = pException.getResponseHeaderMetadata();
		this.fErrorMessage = pException.getMessage();
		this.fErrorMessage += "; StatusCode: " + pException.getStatusCode();
		this.fErrorMessage += "; ErrorCode: " + pException.getErrorCode();
		this.fErrorMessage += "; ErrorType: " + pException.getErrorType();
	}

	public MwsMethodResponse(ResponseHeaderMetadata pHeaderMetdata, String pErrorMessage)
	{
		this.fHeaderMetadata = pHeaderMetdata;
		this.fErrorMessage = pErrorMessage;
	}

	public boolean isError()
	{
		return this.fErrorMessage != null;
	}

	public String getErrorMessage()
	{
		return this.fErrorMessage;
	}

	// Header information
	public String getHeaderRequestId()
	{
		return this.fHeaderMetadata.getRequestId();
	}

	public String getHeaderTimestamp()
	{
		return this.fHeaderMetadata.getTimestamp();
	}

	public Double getHeaderQuotaMax()
	{
		return this.fHeaderMetadata.getQuotaMax();
	}

	public Double getHeaderQuotaRemaining()
	{
		return this.fHeaderMetadata.getQuotaRemaining();
	}

	public Date getHeaderQuotaResetsAt()
	{
		return this.fHeaderMetadata.getQuotaResetsAt();
	}

	// End

}
