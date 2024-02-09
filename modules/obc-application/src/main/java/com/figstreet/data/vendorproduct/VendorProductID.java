package com.figstreet.data.vendorproduct;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.Logging;
import com.figstreet.core.LongDataID;
import com.figstreet.core.LongDataIDTypeDescriptor;

public class VendorProductID extends LongDataID
{
	private static final long serialVersionUID = -2943124728126137070L;

	private static final String LOGGER_NAME = VendorProductID.class.getPackage().getName() + ".VendorProductID";

	public static final LongDataIDTypeDescriptor<VendorProductID> VENDORPRODUCTID_DESCRIPTOR = new LongDataIDTypeDescriptor<>(
			VendorProductID.class);

	public VendorProductID()
	{
		super(VENDORPRODUCTID_DESCRIPTOR);
	}

	public VendorProductID(Long pValue)
	{
		super(pValue, VENDORPRODUCTID_DESCRIPTOR);
	}

	public VendorProductID(String pValue)
	{
		super(pValue, VENDORPRODUCTID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		VendorProductID newID = null;
		try
		{
			newID = new VendorProductID(pResultSet.getLong(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}
}