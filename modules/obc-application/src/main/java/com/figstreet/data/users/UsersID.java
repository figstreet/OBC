package com.figstreet.data.users;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class UsersID extends IntDataID
{
	private static final long serialVersionUID = 20398154868732095L;
	private static final String LOGGER_NAME = UsersID.class.getPackage().getName() + ".UsersID";

	public static final IntDataIDTypeDescriptor<UsersID> USERSID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(UsersID.class);


	public static final UsersID ADMIN = new UsersID("100");

	public UsersID()
	{
		super(USERSID_DESCRIPTOR);
	}

	public UsersID(Integer pValue)
	{
		super(pValue, USERSID_DESCRIPTOR);
	}

	public UsersID(String pValue)
	{
		super(pValue, USERSID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		UsersID newID = null;
		try
		{
			newID = new UsersID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading UsersID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}
}
