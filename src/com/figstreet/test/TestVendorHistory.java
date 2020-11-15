package com.figstreet.test;

import com.figstreet.biz.vendor.VendorHistory;
import com.figstreet.biz.vendor.VendorManager;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;

public class TestVendorHistory
{
	public static void main(String[] args) throws Exception
	{
		String fWebsite;

		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			VendorID test = new VendorID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();

			Vendor vendor = Vendor.getByVendorID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Name in Vendor " + test.getValue() + " is " + vendor.getName());

			VendorHistory vendorHistory = new VendorHistory(test, vendor);
			String fJSONString = vendorHistory.toJsonString(true);
			VendorHistory vendorHistory1 = VendorHistory.newInstance(fJSONString);

			if (vendorHistory.toJsonString().equals(vendorHistory1.toJsonString()))
				System.out.println("Both the object instances have same properties value in JSON string");
			else
				System.out.println("Both the object instances do not have same properties value in JSON string");

			Vendor lastVendor = (Vendor) vendor.clone();
			System.out.println("Value of Website in Cloned Vendor is " + test.getValue() + " is " + lastVendor.getWebsite());

			fWebsite = "www.website1.com";
			vendor.setWebsite(fWebsite);
			trans.commit();
			trans.close();

			VendorManager.updateVendor(vendor, lastVendor, UsersID.ADMIN);
			System.out.println("Updated Website for Vendor ID " + test.getValue() + " is " + vendor.getWebsite());
		}

		finally
		{
			SystemInitializer.shutdown();
		}
	}

	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}