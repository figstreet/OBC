package com.figstreet.data.userpermission;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class UserPermissionID extends IntDataID
{
	
	private static final long serialVersionUID = -7097105001529324189L;

	private static final String LOGGER_NAME = UserPermissionID.class.getPackage().getName() + ".UserPermissionID";

	public static final IntDataIDTypeDescriptor<UserPermissionID> USERPERMISSIONID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			UserPermissionID.class);

	public UserPermissionID()
	{
		super(USERPERMISSIONID_DESCRIPTOR);
	}

	public UserPermissionID(Integer pValue)
	{
		super(pValue, USERPERMISSIONID_DESCRIPTOR);
	}

	public UserPermissionID(String pValue)
	{
		super(pValue, USERPERMISSIONID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		UserPermissionID newID = null;
		try
		{
			newID = new UserPermissionID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
