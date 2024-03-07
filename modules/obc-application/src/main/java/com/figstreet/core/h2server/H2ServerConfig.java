package com.figstreet.core.h2server;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.Logging;
import com.figstreet.core.crypto.SecureXmlConfigReader;
import org.apache.commons.codec.digest.HmacAlgorithms;

public class H2ServerConfig extends SecureXmlConfigReader
{
	public H2ServerConfig(File pConfigFile) throws IOException, NoSuchAlgorithmException
	{
		super(pConfigFile);
	}

	@Override
	protected String getLoggerName()
	{
		return LOGGER_NAME;
	}

	public String getLog4JConf()
	{
		return this.getValueStr("log4jconf");
	}

	public String getKeyStoreFile()
	{
		return this.getValueStr("keyStoreFile");
	}

	public String getKeyStoreType()
	{
		return this.getValueStr("keyStoreType");
	}

	public String getKeyStoreEncodingType()
	{
		return this.getValueStr("keyStoreEncodingType");
	}


	public char[] getKeyStoreSalt()
	{
		return this.getValue("keyStoreSalt");
	}

	public char[] getKeyStoreKey()
	{
		return this.getValue("keyStoreKey");
	}

	public char[] getKeyStoreCode()
	{
		return this.getValue("keyStoreCode");
	}

	public HmacAlgorithms getKeyStoreAlgorithm()
	{
		HmacAlgorithms algorithm = HmacAlgorithms.HMAC_SHA_256;
		String value = this.getValueStr("keyStoreAlgorithm");
		if (!CompareUtil.isEmpty(value))
			algorithm = HmacAlgorithms.valueOf(value);
		return algorithm;
	}

	public int getRunMainEverySecs()
	{
		return Integer.parseInt(this.getValueStr("runMainEverySecs"));
	}

	public long getRunMainEveryMillis()
	{
		return this.getRunMainEverySecs() * DateUtil.MILLIS_PER_SECOND;
	}

	public boolean getConfigChangeRestartsServices()
	{
		return Boolean.parseBoolean(this.getValueStr("restartServicesOnConfigChange"));
	}

	public int getRunProcessorEverySecs()
	{
		return Integer.parseInt(this.getValueStr("runProcessorEverySecs"));
	}

	public long getRunProcessorEveryMillis()
	{
		return this.getRunProcessorEverySecs() * DateUtil.MILLIS_PER_SECOND;
	}

	public boolean isStartWebServer()
	{
		return Boolean.parseBoolean(this.getValueStr("startWebServer"));
	}

	public Integer getWebServerPort()
	{
		Integer port = null;
		String value = this.getValueStr("webPort");
		try
		{
			port = new Integer(value);
		}
		catch (NumberFormatException nfe)
		{
			port = null;
			Logging.error(LOGGER_NAME, "getWebConsolePort", "Value is not an integer: " + value);
		}

		return port;
	}

	public boolean getWebServerUseSSL()
	{
		return Boolean.parseBoolean(this.getValueStr("webSSL"));
	}

	public boolean getWebServerAllowOthers()
	{
		return Boolean.parseBoolean(this.getValueStr("webAllowOthers"));
	}

	public boolean isStartTcpDatabaseServer()
	{
		return Boolean.parseBoolean(this.getValueStr("startTcpDatabaseServer"));
	}

	public Integer getTcpServerPort()
	{
		Integer port = null;
		String value = this.getValueStr("tcpPort");
		try
		{
			port = new Integer(value);
		}
		catch (NumberFormatException nfe)
		{
			port = null;
			Logging.error(LOGGER_NAME, "getWebConsolePort", "Value is not an integer: " + value);
		}

		return port;
	}

	public boolean getTcpServerUseSSL()
	{
		return Boolean.parseBoolean(this.getValueStr("tcpSSL"));
	}

	public boolean getTcpServerAllowOthers()
	{
		return Boolean.parseBoolean(this.getValueStr("tcpAllowOthers"));
	}

	public boolean getTcpServerAllowDatabaseCreation()
	{
		return Boolean.parseBoolean(this.getValueStr("tcpAllowDatabaseCreation"));
	}

	public String getDatabaseInitURL()
	{
		return this.getValueStr("databaseInitURL");
	}

	public char[] getAdminUser()
	{
		return this.getValue("adminUser");
	}

	public char[] getAdminPassword()
	{
		return this.getValue("adminPassword");
	}

	public Integer getPageCacheKB()
	{
		Integer cacheKB = null;
		String value = this.getValueStr("pageCacheKB");
		try
		{
			cacheKB = new Integer(value);
		}
		catch (NumberFormatException nfe)
		{
			cacheKB = null;
			Logging.error(LOGGER_NAME, "getPageCacheKB", "Value is not an integer: " + value);
		}

		return cacheKB;
	}

	private static final String LOGGER_NAME = H2ServerConfig.class.getPackage().getName() + ".H2ServerConfig";
}
