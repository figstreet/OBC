package com.figstreet.data.vendoraddress;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class VendorAddressID extends IntDataID
{

	private static final long serialVersionUID = -3899198189447881773L;

	private static final String LOGGER_NAME = VendorAddressID.class.getPackage().getName() + ".VendorAddressID";

	public static final IntDataIDTypeDescriptor<VendorAddressID> VENDORADDRESSID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			VendorAddressID.class);

	public VendorAddressID()
	{
		super(VENDORADDRESSID_DESCRIPTOR);
	}

	public VendorAddressID(Integer pValue)
	{
		super(pValue, VENDORADDRESSID_DESCRIPTOR);
	}

	public VendorAddressID(String pValue)
	{
		super(pValue, VENDORADDRESSID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		VendorAddressID newID = null;
		try
		{
			newID = new VendorAddressID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
