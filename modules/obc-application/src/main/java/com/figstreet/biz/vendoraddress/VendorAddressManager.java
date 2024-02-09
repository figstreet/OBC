package com.figstreet.biz.vendoraddress;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendoraddress.VendorAddress;

public class VendorAddressManager
{
	public static List<String> updateVendorAddress(VendorAddress pVendorAddress, VendorAddress pLastVendorAddress,
			UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pVendorAddress == null)
			messages.add("VendorAddress cannot be null");

		// TODO - check permissions for updating a vendoraddress

		if (messages.isEmpty())
		{
			History vendorAddressHistory = VendorAddressHistory.buildVendorAddressHistory(pVendorAddress,
					pLastVendorAddress, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pVendorAddress.saveOrUpdate(pBy);
				if (vendorAddressHistory != null)
					vendorAddressHistory.saveOrUpdate(pBy);
				trans.commit();
			} finally
			{
				trans.close();
			}
		}
		return messages;
	}
}
