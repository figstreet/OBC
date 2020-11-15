package com.figstreet.data.vendorcontact;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.figstreet.core.IntDataID;
import com.figstreet.core.IntDataIDTypeDescriptor;
import com.figstreet.core.Logging;

public class VendorContactID extends IntDataID
{
	private static final long serialVersionUID = -5128934039589289423L;

	private static final String LOGGER_NAME = VendorContactID.class.getPackage().getName() + ".VendorContactID";

	public static final IntDataIDTypeDescriptor<VendorContactID> VENDORCONTACTID_DESCRIPTOR = new IntDataIDTypeDescriptor<>(
			VendorContactID.class);

	public VendorContactID()
	{
		super(VENDORCONTACTID_DESCRIPTOR);
	}

	public VendorContactID(Integer pValue)
	{
		super(pValue, VENDORCONTACTID_DESCRIPTOR);
	}

	public VendorContactID(String pValue)
	{
		super(pValue, VENDORCONTACTID_DESCRIPTOR);
	}

	@Override
	public Serializable consumeIdentifier(ResultSet pResultSet)
	{
		VendorContactID newID = null;
		try
		{
			newID = new VendorContactID(pResultSet.getInt(1));
		}
		catch (SQLException e)
		{
			Logging.error(LOGGER_NAME, "consumeIdentifier",
					"Error reading ID from ResultSet at index 1; returning null", e);
		}
		return newID;
	}

}
