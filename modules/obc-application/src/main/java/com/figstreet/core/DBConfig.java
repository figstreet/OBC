package com.figstreet.core;

import com.figstreet.data.configvalue.ConfigValue;
import com.figstreet.data.configvalue.ConfigValueList;
import com.figstreet.data.users.UsersID;

import javax.mail.search.SearchException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DBConfig extends RefreshCache implements Serializable
{
	private static final String LOGGER_NAME = DBConfig.class.getPackage().getName() + ".DBConfig";
	private String fConfigID;
	private String fConfigName;
	private ClientID fClientID;
	private HashMap<String, String> fProperties;

	/**
	 * @param pConfigName
	 */
	public DBConfig(String pConfigName, ClientID pClientID)
	{
		super(pConfigName + "_" + pClientID);
		this.fConfigID = pConfigName + "_" + pClientID;
		this.fConfigName = pConfigName;
		this.fClientID = pClientID;
		try
		{
			this.loadValues();
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "ctor", e);
		}
	}

	@Override
	public String getLoggerName()
	{
		return LOGGER_NAME;
	}

	public boolean isInitialized()
	{
		return this.fProperties != null;
	}

	public String getConfigID()
	{
		return this.fConfigID;
	}

	public String getConfigName()
	{
		return this.fConfigName;
	}

	public ClientID getClientID()
	{
		return this.fClientID;
	}

	protected void setProperty(UsersID pBy, String pPropertyName, String pValue)
			throws SQLException, SearchException {
		ConfigValue value = ConfigValue.findByNameForClient(this.fClientID, this.fConfigName, pPropertyName);
		if (value != null)
		{
			value.setPropertyValue(pValue);
		}
		else
		{
			value = new ConfigValue(this.fClientID, this.fConfigName, pPropertyName, pValue, pBy);
		}
		value.saveOrUpdate(pBy);
		synchronized (this.fProperties)
		{
			this.fProperties.put(value.getPropertyName(), value.getPropertyValue());
		}
		this.markCacheModified(pBy);
	}

	protected boolean setPropertyOnChange(UsersID pBy, String pPropertyName, String pValue)
		throws SQLException, SearchException
	{
		boolean doUpdate = false;
		ConfigValue value = ConfigValue.findByNameForClient(this.fClientID, this.fConfigName, pPropertyName);
		if (value != null)
		{
			if (!CompareUtil.equalsString(pValue, value.getPropertyValue(), true))
			{
				doUpdate = true;
				value.setPropertyValue(pValue);
			}
		}
		else
		{
			value = new ConfigValue(this.fClientID, this.fConfigName, pPropertyName, pValue, pBy);
			doUpdate = true;
		}

		if (doUpdate)
		{
			synchronized (this.fProperties)
			{
				value.saveOrUpdate(pBy);
				this.fProperties.put(value.getPropertyName(), value.getPropertyValue());
			}
			this.markCacheModified(pBy);
		}

		return doUpdate;
	}

	protected String getProperty(String pPropertyName)
	{
		if (!this.fProperties.containsKey(pPropertyName))
			throw new IllegalArgumentException(String.format("property(%s) not found", pPropertyName));

		return this.fProperties.get(pPropertyName);
	}

	protected String findProperty(String pPropertyName)
	{
		return this.fProperties.get(pPropertyName);
	}

	private void loadValues() throws SQLException, SearchException
	{
		ConfigValueList values = ConfigValueList.loadByTypeAndNameForClient(this.fClientID, this.fConfigName, null);
		synchronized (this)
		{
			HashMap<String, String> properties = new HashMap<>();
			for (ConfigValue cv : values)
			{
				properties.put(cv.getPropertyName(), cv.getPropertyValue());
			}
			this.fProperties = properties;
		}
	}

	protected Collection<String> findProperties(String pPropertyNamePrefix)
	{
		TreeMap<String, String> sortedMap = new TreeMap<>();
		if (!CompareUtil.isEmpty(pPropertyNamePrefix))
		{
			String prefix = pPropertyNamePrefix.toLowerCase();
			for (Map.Entry<String, String> entry : this.fProperties.entrySet())
			{
				if (entry != null && !CompareUtil.isEmpty(entry.getKey()))
				{
					String propertyName = entry.getKey().toLowerCase();
					if (propertyName.startsWith(prefix))
					{
						String remainder = propertyName.substring(prefix.length());
						try
						{
							Integer.parseInt(remainder);
							sortedMap.put(entry.getKey(), entry.getValue());
						}
						catch (NumberFormatException nfe)
						{
							//do nothing
						}
					}
				}
			}
		}

		return sortedMap.values();
	}

	protected List<Map.Entry<String, String>> findPropertyEntries(String pPropertyNamePrefix)
	{
		ArrayList<Map.Entry<String, String>> propertyList = new ArrayList<>();
		if (!CompareUtil.isEmpty(pPropertyNamePrefix))
		{
			String prefix = pPropertyNamePrefix.toLowerCase();
			for (Map.Entry<String, String> entry : this.fProperties.entrySet())
			{
				if (entry != null && !CompareUtil.isEmpty(entry.getKey()))
				{
					String propertyName = entry.getKey().toLowerCase();
					if (propertyName.startsWith(prefix))
						propertyList.add(entry);
				}
			}
		}
		return propertyList;
	}

	@Override
	public void refresh()
	{
		try
		{
			this.loadValues();
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "refresh", e);
		}
	}

}
