package com.figstreet.data.amazonpricing;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.Logging;
import com.figstreet.core.LongDataID;
import com.figstreet.core.LongDataIDTypeDescriptor;

public class AmazonPricingID extends LongDataID
{

	private static final long serialVersionUID = -6953556262772427895L;

	private static final String LOGGER_NAME = AmazonPricingID.class.getPackage().getName() + ".AmazonPricingID";

	public static final LongDataIDTypeDescriptor<AmazonPricingID> AMAZONPRICINGID_DESCRIPTOR = new LongDataIDTypeDescriptor<>(
			AmazonPricingID.class);

	public AmazonPricingID()
	{
		super(AMAZONPRICINGID_DESCRIPTOR);
	}

	public AmazonPricingID(Long pValue)
	{
		super(pValue, AMAZONPRICINGID_DESCRIPTOR);
	}

	public AmazonPricingID(String pValue)
	{
		super(pValue, AMAZONPRICINGID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		AmazonPricingID newID = null;
		try
		{
			newID = new AmazonPricingID(pResultSet.getLong(1));
		} catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}
}