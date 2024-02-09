package com.figstreet.biz.dataexchange.amazonmws;

import com.amazonservices.mws.products.MWSEndpoint;


//TODO - this class's data should really be stored in ConfigValues

public class MarketplaceWebServiceConfig
{
	/** Developer AWS access key. */
	private String fAwsAccessKey = "AKIAI4WFST6BCANELVYQ";

	/** Developer AWS secret key. */
	private String fAwsSecretKey = "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";

	/** The client application name. */
	private String fAppName = "OBC";

	/** The client application version. */
	private String fAppVersion = "0.1";

	private String fServiceURL = MWSEndpoint.NA_PROD.toString();

	private int fMaxGetMatchingProductForId = 5;

	public MarketplaceWebServiceConfig()
	{
		// Load from config settings
	}

	public MarketplaceWebServiceConfig(String pAwsAccessKey, String pAwsSecretKey, String pAppName, String pAppVersion,
			String pServiceURL)
	{
		this.fAwsAccessKey = pAwsAccessKey;
		this.fAwsSecretKey = pAwsSecretKey;
		this.fAppName = pAppName;
		this.fAppVersion = pAppVersion;
		this.fServiceURL = pServiceURL;
	}

	public String getAwsAccessKey()
	{
		return this.fAwsAccessKey;
	}

	public String getAwsSecretKey()
	{
		return this.fAwsSecretKey;
	}

	public String getAppName()
	{
		return this.fAppName;
	}

	public String getAppVersion()
	{
		return this.fAppVersion;
	}

	public String getServiceURL()
	{
		return this.fServiceURL;
	}

	public int getMaxGetMatchingProductForId()
	{
		return this.fMaxGetMatchingProductForId;
	}

}
