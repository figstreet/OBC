package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendoraddress.VendorAddress;
import com.figstreet.data.vendoraddress.VendorAddressID;
import com.figstreet.data.vendoraddress.VendorAddressType;

public class TestVendorAddress
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

			VendorAddressID test = new VendorAddressID("2");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			VendorAddress testVendorAddress = VendorAddress.getByVendorAddressID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms; Zip: " + testVendorAddress.getZip() + " Type: " + VendorAddressType.asString(testVendorAddress.getType()));

			testVendorAddress.saveOrUpdate(UsersID.ADMIN);
			trans.commit();
			trans.close();


			// TODO: To test Insert, Update and Delete, modify constructor
//			VendorAddress vendorAddress = new VendorAddress(VendorID.newInstance(3), UsersID.ADMIN);
//			startMS = System.currentTimeMillis();
//			vendorAddress.saveOrUpdate(null, UsersID.ADMIN);
//			endMS = System.currentTimeMillis();
//			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + vendorAddress.getRecordID());
//
//			startMS = System.currentTimeMillis();
//			vendorAddress.saveOrUpdate(null, UsersID.ADMIN);
//			endMS = System.currentTimeMillis();
//			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + vendorAddress.getRecordID());
//
//			startMS = System.currentTimeMillis();
//			vendorAddress.delete(null);
//			endMS = System.currentTimeMillis();
//			System.out.println("Deleted in " + (endMS - startMS) + " ms ");
//
//			startMS = System.currentTimeMillis();
//			trans = HibernateTransaction.open();
//			vendorAddress = new VendorAddress(VendorID.newInstance(4), UsersID.ADMIN);
//			vendorAddress.saveOrUpdate(trans, UsersID.ADMIN);
//			System.out.println("Inserted: " + vendorAddress.getRecordID());
//			vendorAddress.saveOrUpdate(trans, UsersID.ADMIN);
//			System.out.println("Updated: " + vendorAddress.getRecordID());
//			trans.commit();
//			// trans.rollback();
//			trans.close();
//			endMS = System.currentTimeMillis();
//			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
