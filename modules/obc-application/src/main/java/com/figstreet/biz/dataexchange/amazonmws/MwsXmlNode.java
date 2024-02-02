package com.figstreet.biz.dataexchange.amazonmws;

import java.util.LinkedHashMap;

public class MwsXmlNode
{
	public static final String NAME_DELIMITER = "\u0003";

	private String fFullName;
	private String fName;
	private String fValue;

	private LinkedHashMap<String, String> fAttributeMap = new LinkedHashMap<>(20);

	public MwsXmlNode(String pName, String pFullName, String pValue)
	{
		this.fName = pName;
		this.fFullName = pFullName;
		this.fValue = pValue;
	}

	public String getFullName()
	{
		return this.fFullName;
	}

	public String getName()
	{
		return this.fName;
	}

	public String getValue()
	{
		return this.fValue;
	}

	public void setValue(String pValue)
	{
		this.fValue = pValue;
	}

	public void addAttribute(String pName, String pValue)
	{
		this.fAttributeMap.put(pName, pValue);
	}

	public String findAttribute(String pName)
	{
		return this.fAttributeMap.get(pName);
	}

	@Override
	public String toString()
	{
		String strValue = "";
		if (this.fName != null)
			strValue += this.fName;
		strValue += "=";
		if (this.fValue != null)
			strValue += this.fValue;
		return strValue;
	}
}
