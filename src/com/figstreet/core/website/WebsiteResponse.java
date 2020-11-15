package com.figstreet.core.website;

import java.io.File;

public class WebsiteResponse
{
	private int fHttpStatusCode;
	private String fLocation;
	private File fResponseFile;

	public WebsiteResponse(int pHttpStatusCode, String pLocation)
	{
		this.fHttpStatusCode = pHttpStatusCode;
		this.fLocation = pLocation;
	}

	public int getHttpStatusCode()
	{
		return fHttpStatusCode;
	}

	public void setHttpStatusCode(int pHttpStatusCode)
	{
		this.fHttpStatusCode = pHttpStatusCode;
	}

	public String getLocation()
	{
		return fLocation;
	}

	public void setLocation(String pLocation)
	{
		this.fLocation = pLocation;
	}

	public File getResponseFile()
	{
		return fResponseFile;
	}

	public void setResponseFile(File pResponseFile)
	{
		this.fResponseFile = pResponseFile;
	}
	
}