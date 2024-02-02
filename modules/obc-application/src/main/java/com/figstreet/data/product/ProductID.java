package com.figstreet.data.product;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class ProductID extends IntDataID
{

	private static final long serialVersionUID = 2196616702389191669L;

	private static final String LOGGER_NAME = ProductID.class.getPackage().getName() + ".ProductID";

	public static final IntDataIDTypeDescriptor<ProductID> PRODUCTID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			ProductID.class);

	public ProductID()
	{
		super(PRODUCTID_DESCRIPTOR);
	}

	public ProductID(Integer pValue)
	{
		super(pValue, PRODUCTID_DESCRIPTOR);
	}

	public ProductID(String pValue)
	{
		super(pValue, PRODUCTID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		ProductID newID = null;
		try
		{
			newID = new ProductID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
