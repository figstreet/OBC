package com.figstreet.core.website;

public class WebFatalException extends Exception
{
	public WebFatalException()
	{
		super();
	}

	public WebFatalException(Exception pException)
	{
		super(pException);
	}

	public WebFatalException(String pMessage)
	{
		super(pMessage);
	}
}
