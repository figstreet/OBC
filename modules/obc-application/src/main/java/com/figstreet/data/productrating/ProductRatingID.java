package com.figstreet.data.productrating;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.Logging;
import com.figstreet.core.LongDataID;
import com.figstreet.core.LongDataIDTypeDescriptor;

public class ProductRatingID extends LongDataID
{

	private static final long serialVersionUID = 8739612574702462471L;

	private static final String LOGGER_NAME = ProductRatingID.class.getPackage().getName() + ".ProductRatingID";

	public static final LongDataIDTypeDescriptor<ProductRatingID> PRODUCTRATINGID_DESCRIPTOR = new LongDataIDTypeDescriptor<>(
			ProductRatingID.class);

	public ProductRatingID()
	{
		super(PRODUCTRATINGID_DESCRIPTOR);
	}

	public ProductRatingID(Long pValue)
	{
		super(pValue, PRODUCTRATINGID_DESCRIPTOR);
	}

	public ProductRatingID(String pValue)
	{
		super(pValue, PRODUCTRATINGID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		ProductRatingID newID = null;
		try
		{
			newID = new ProductRatingID(pResultSet.getLong(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
