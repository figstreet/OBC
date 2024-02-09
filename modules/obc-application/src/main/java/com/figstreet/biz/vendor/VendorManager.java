package com.figstreet.biz.vendor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;

public class VendorManager
{
	public static List<String> updateVendor(Vendor pVendor, Vendor pLastVendor, UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pVendor == null)
			messages.add("Vendor cannot be null");

		// TODO - check permissions for updating a product

		if (messages.isEmpty())
		{
			History vendorHistory = VendorHistory.buildVendorHistory(pVendor, pLastVendor, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pVendor.saveOrUpdate(pBy);
				if (vendorHistory != null)
					vendorHistory.saveOrUpdate(pBy);
				trans.commit();
			} finally
			{
				trans.close();
			}
		}
		return messages;
	}
}
