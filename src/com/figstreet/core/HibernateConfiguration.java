package com.figstreet.core;

import java.net.URL;

public class HibernateConfiguration
{
	private static URL CONFIG_URL;

	private HibernateConfiguration()
	{
		// empty ctor
	}

	public static void initialize(URL pConfigURL)
	{
		CONFIG_URL = pConfigURL;
	}

	public static URL getConfigurationURL()
	{
		return CONFIG_URL;
	}
}
