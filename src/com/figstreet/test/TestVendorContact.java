package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorcontact.VendorContact;
import com.figstreet.data.vendorcontact.VendorContactID;
import com.figstreet.data.vendorcontact.VendorContactType;

public class TestVendorContact
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

			VendorContactID test = new VendorContactID("4");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			VendorContact testVendorContact = VendorContact.getByVendorContactID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testVendorContact.getEmail());
			trans.commit();
			trans.close();

			// TODO: To test Insert, Update and Delete, modify constructor
			VendorContact vendorContact = new VendorContact(new VendorID(3), VendorContactType.SALES,
					UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			vendorContact.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + vendorContact.getRecordID());

			startMS = System.currentTimeMillis();
			vendorContact.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + vendorContact.getRecordID());

			startMS = System.currentTimeMillis();
			vendorContact.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			vendorContact = new VendorContact(new VendorID(4), VendorContactType.SALES, UsersID.ADMIN);
			vendorContact.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + vendorContact.getRecordID());
			vendorContact.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + vendorContact.getRecordID());
			trans.commit();
			// trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
