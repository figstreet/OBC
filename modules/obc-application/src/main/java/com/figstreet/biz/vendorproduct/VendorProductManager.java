package com.figstreet.biz.vendorproduct;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProduct;

public class VendorProductManager
{
	public static List<String> updateVendorProduct(VendorProduct pVendorProduct, VendorProduct pLastVendorProduct, UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pVendorProduct == null)
			messages.add("VendorProduct cannot be null");

		//TODO - check permissions for updating a vendorproduct

		if (messages.isEmpty())
		{
			History vendorProductHistory = VendorProductHistory.buildVendorProductHistory(pVendorProduct, pLastVendorProduct, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pVendorProduct.saveOrUpdate(pBy);
				if (vendorProductHistory != null)
					vendorProductHistory.saveOrUpdate(pBy);
				trans.commit();
			}
			finally
			{
				trans.close();
			}
		}
		return messages;
	}
}
