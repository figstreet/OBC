package com.figstreet.test;

import com.figstreet.biz.vendorcontact.VendorContactHistory;
import com.figstreet.biz.vendorcontact.VendorContactManager;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorcontact.VendorContact;
import com.figstreet.data.vendorcontact.VendorContactID;

public class TestVendorContactHistory
{

	public static void main(String[] args) throws Exception
	{
		String fName1;

		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			VendorContactID test = new VendorContactID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();

			VendorContact vendorContact = VendorContact.getByVendorContactID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Name1 in VendorContact " + test.getValue() + " is " + vendorContact.getName1());

			VendorContactHistory vendorContactHistory = new VendorContactHistory(test, vendorContact);
			String fJSONString = vendorContactHistory.toJsonString(true);
			VendorContactHistory vendorContactHistory1 = VendorContactHistory.newInstance(fJSONString);

			if (vendorContactHistory.toJsonString().equals(vendorContactHistory1.toJsonString()))
				System.out.println("Both the object instances have same properties value in JSON string");
			else
				System.out.println("Both the object instances do not have same properties value in JSON string");

			VendorContact lastVendorContact = (VendorContact) vendorContact.clone();
			System.out.println("Value of Name1 in Cloned VendorContact is " + test.getValue() + " is "
					+ lastVendorContact.getName1());

			fName1 = "NewNamehereagain";
			vendorContact.setName1(fName1);
			trans.commit();
			trans.close();

			VendorContactManager.updateVendorContact(vendorContact, lastVendorContact, UsersID.ADMIN);
			System.out.println(
					"Updated Name1 for VendorContact ID " + test.getValue() + " is " + vendorContact.getName1());
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