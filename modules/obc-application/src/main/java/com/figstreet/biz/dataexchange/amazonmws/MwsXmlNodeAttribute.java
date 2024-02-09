package com.figstreet.biz.dataexchange.amazonmws;

public class MwsXmlNodeAttribute
{
	private String fName;
	private String fValue;

	public MwsXmlNodeAttribute(String pName, String pValue)
	{
		this.fName = pName;
		this.fValue = pValue;
	}

	public String getName()
	{
		return this.fName;
	}

	public String getValue()
	{
		return this.fValue;
	}

}
