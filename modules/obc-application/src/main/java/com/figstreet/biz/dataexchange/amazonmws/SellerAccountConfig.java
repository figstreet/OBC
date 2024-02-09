package com.figstreet.biz.dataexchange.amazonmws;

//import com.amazonservices.mws.client.MwsMarketplaceID;

//TODO - this class's data should really be stored in ConfigValues

public class SellerAccountConfig
{
	private String fSellerId = "A3QVQ2KST6QIOM";
	private String fMwsAuthToken = "amzn.mws.e4b93938-5743-7c4d-3cb1-020531deef72";
	//private String fMarketplaceId = MwsMarketplaceID.US;
	private String fMarketplaceId = "US";

	public SellerAccountConfig()
	{
		//nothing yet
	}

	public SellerAccountConfig(String pSellerId, String pMwsAuthToken, String pMarketplaceId)
	{
		this.fSellerId = pSellerId;
		this.fMwsAuthToken = pMwsAuthToken;
		this.fMarketplaceId = pMarketplaceId;
	}

	public String getSellerId()
	{
		return this.fSellerId;
	}

	public String getMwsAuthToken()
	{
		return this.fMwsAuthToken;
	}

	public String getMarketplaceId()
	{
		return this.fMarketplaceId;
	}

}
