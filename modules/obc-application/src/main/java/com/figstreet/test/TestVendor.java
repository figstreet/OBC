package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;

public class TestVendor
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			VendorID test = new VendorID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			Vendor testVendor = Vendor.getByVendorID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testVendor.getName());

			Vendor vendor = new Vendor("Amazon (US)", UsersID.ADMIN);
			vendor.setActive(true);
			vendor.setWebsite("https://www.amazon.com");
			startMS = System.currentTimeMillis();
			vendor.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + vendor.getRecordID());
			trans.commit();
			trans.close();

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			vendor = new Vendor("Amazon (IN)", UsersID.ADMIN);
			vendor.setActive(true);
			vendor.setWebsite("https://www.amazon.in");
			vendor.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + vendor.getRecordID());
			vendor.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + vendor.getRecordID());
			trans.commit();
			// trans.rollback();
			trans.close();

//			startMS = System.currentTimeMillis();
//			vendor.saveOrUpdate(null, UsersID.ADMIN);
//			endMS = System.currentTimeMillis();
//			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + vendor.getRecordID());
//
//			startMS = System.currentTimeMillis();
//			vendor.delete(null);
//			endMS = System.currentTimeMillis();
//			System.out.println("Deleted in " + (endMS - startMS) + " ms ");
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
