package com.figstreet.data.amazonsalesrank;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.Logging;
import com.figstreet.core.LongDataID;
import com.figstreet.core.LongDataIDTypeDescriptor;

public class AmazonSalesRankID extends LongDataID
{
	private static final long serialVersionUID = 4003927996699599151L;

	private static final String LOGGER_NAME = AmazonSalesRankID.class.getPackage().getName() + ".AmazonSalesRankID";

	public static final LongDataIDTypeDescriptor<AmazonSalesRankID> AMAZONSALESRANKID_DESCRIPTOR = new LongDataIDTypeDescriptor<>(
			AmazonSalesRankID.class);

	public AmazonSalesRankID()
	{
		super(AMAZONSALESRANKID_DESCRIPTOR);
	}

	public AmazonSalesRankID(Long pValue)
	{
		super(pValue, AMAZONSALESRANKID_DESCRIPTOR);
	}

	public AmazonSalesRankID(String pValue)
	{
		super(pValue, AMAZONSALESRANKID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		AmazonSalesRankID newID = null;
		try
		{
			newID = new AmazonSalesRankID(pResultSet.getLong(1));
		} catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}
}