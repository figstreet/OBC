package com.figstreet.data.productoption;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class ProductOptionID extends IntDataID
{

	private static final long serialVersionUID = 3179194459485391493L;

	private static final String LOGGER_NAME = ProductOptionID.class.getPackage().getName() + ".ProductOptionID";

	public static final IntDataIDTypeDescriptor<ProductOptionID> PRODUCTOPTIONID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			ProductOptionID.class);

	public ProductOptionID()
	{
		super(PRODUCTOPTIONID_DESCRIPTOR);
	}

	public ProductOptionID(Integer pValue)
	{
		super(pValue, PRODUCTOPTIONID_DESCRIPTOR);
	}

	public ProductOptionID(String pValue)
	{
		super(pValue, PRODUCTOPTIONID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		ProductOptionID newID = null;
		try
		{
			newID = new ProductOptionID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
