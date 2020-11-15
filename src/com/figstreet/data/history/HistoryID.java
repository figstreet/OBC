package com.figstreet.data.history;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class HistoryID extends IntDataID
{
	private static final long serialVersionUID = -2841569954809025513L;

	private static final String LOGGER_NAME = HistoryID.class.getPackage().getName() + ".HistoryID";

	public static final IntDataIDTypeDescriptor<HistoryID> HISTORYID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			HistoryID.class);

	public HistoryID()
	{
		super(HISTORYID_DESCRIPTOR);
	}

	public HistoryID(Integer pValue)
	{
		super(pValue, HISTORYID_DESCRIPTOR);
	}

	public HistoryID(String pValue)
	{
		super(pValue, HISTORYID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		HistoryID newID = null;
		try
		{
			newID = new HistoryID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
