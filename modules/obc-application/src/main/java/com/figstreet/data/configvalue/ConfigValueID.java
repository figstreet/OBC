package com.figstreet.data.configvalue;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class ConfigValueID extends IntDataID
{
	private static final long serialVersionUID = 6756874208190147785L;

	private static final String LOGGER_NAME = ConfigValueID.class.getPackage().getName() + ".ConfigValueID";

	public static final IntDataIDTypeDescriptor<ConfigValueID> CONFIGVALUEID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			ConfigValueID.class);

	public ConfigValueID()
	{
		super(CONFIGVALUEID_DESCRIPTOR);
	}

	public ConfigValueID(Integer pValue)
	{
		super(pValue, CONFIGVALUEID_DESCRIPTOR);
	}

	public ConfigValueID(String pValue)
	{
		super(pValue, CONFIGVALUEID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		ConfigValueID newID = null;
		try
		{
			newID = new ConfigValueID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ConfigValueID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
