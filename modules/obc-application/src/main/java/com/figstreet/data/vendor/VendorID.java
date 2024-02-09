package com.figstreet.data.vendor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class VendorID extends IntDataID
{
	private static final long serialVersionUID = 7536202363945909827L;
	private static final String LOGGER_NAME = VendorID.class.getPackage().getName() + ".VendorID";
	private static final IntDataIDTypeDescriptor<VendorID> VENDORID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			VendorID.class);

	public static final VendorID EVEREST_TOYS = new VendorID(1);
	public static final VendorID AMAZON_US = new VendorID(2);

	public VendorID()
	{
		super(VENDORID_DESCRIPTOR);
	}

	public VendorID(Integer pValue)
	{
		super(pValue, VENDORID_DESCRIPTOR);
	}

	public VendorID(String pValue)
	{
		super(pValue, VENDORID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		VendorID newID = null;
		try
		{
			newID = new VendorID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
