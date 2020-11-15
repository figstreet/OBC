package com.figstreet.biz.vendorcontact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorcontact.VendorContact;

public class VendorContactManager
{
	public static List<String> updateVendorContact(VendorContact pVendorContact, VendorContact pLastVendorContact,
			UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pVendorContact == null)
			messages.add("VendorContact cannot be null");

		// TODO - check permissions for updating a vendorcontact

		if (messages.isEmpty())
		{
			History vendorContactHistory = VendorContactHistory.buildVendorContactHistory(pVendorContact,
					pLastVendorContact, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pVendorContact.saveOrUpdate(pBy);
				if (vendorContactHistory != null)
					vendorContactHistory.saveOrUpdate(pBy);
				trans.commit();
			} finally
			{
				trans.close();
			}
		}
		return messages;
	}
}
