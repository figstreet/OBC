package com.figstreet.core;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientID extends IntDataID
{
	private static final String LOGGER_NAME = ClientID.class.getPackage().getName() + ".ClientID";

	public static final ClientID GENERAL = new ClientID(0);
	public static final String GENERAL_DESC = "General";

	public static final IntDataIDTypeDescriptor<ClientID> CLIENTID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			ClientID.class);

	public ClientID()
	{
		super(CLIENTID_DESCRIPTOR);
	}

	public ClientID(int pValue)
	{
		super(pValue, CLIENTID_DESCRIPTOR);
	}

	public ClientID(String pValue)
	{
		super(pValue, CLIENTID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		ClientID newID = null;
		try
		{
			newID = new ClientID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ClientID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

	public boolean isGeneralID()
	{
		return isGeneralID(this);
	}

	public static boolean isGeneralID(ClientID pID)
	{
		return GENERAL.equals(pID);
	}
}
