package com.figstreet.biz.dataexchange.amazonmws;

public class MwsResponseException extends Exception
{
	private static final long serialVersionUID = 8223361499253956340L;

	public MwsResponseException(String pMessage)
	{
		super(pMessage);
	}
}
