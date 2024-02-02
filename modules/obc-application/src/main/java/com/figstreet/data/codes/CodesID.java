package com.figstreet.data.codes;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class CodesID extends IntDataID
{
	private static final long serialVersionUID = -6905987664272074122L;
	private static final String LOGGER_NAME = CodesID.class.getPackage().getName() + ".CodesID";

	public static final IntDataIDTypeDescriptor<CodesID> CODESID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			CodesID.class);

	public CodesID()
	{
		super(CODESID_DESCRIPTOR);
	}

	public CodesID(Integer pValue)
	{
		super(pValue, CODESID_DESCRIPTOR);
	}

	public CodesID(String pValue)
	{
		super(pValue, CODESID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		CodesID newID = null;
		try
		{
			newID = new CodesID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
