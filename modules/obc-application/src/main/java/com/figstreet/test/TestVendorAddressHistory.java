package com.figstreet.test;

import com.figstreet.biz.vendoraddress.VendorAddressHistory;
import com.figstreet.biz.vendoraddress.VendorAddressManager;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendoraddress.VendorAddress;
import com.figstreet.data.vendoraddress.VendorAddressID;

public class TestVendorAddressHistory
{

	public static void main(String[] args) throws Exception
	{
		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			VendorAddressID test = new VendorAddressID("1");
			startMS = System.currentTimeMillis();
			VendorAddress vendorAddress = VendorAddress.getByVendorAddressID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Address1 in VendorAddress " + vendorAddress.getRecordID() + " is " + vendorAddress.getAddress1() + "; loaded in " + (endMS-startMS) + "ms");

			VendorAddressHistory vendorAddressHistory = new VendorAddressHistory(vendorAddress.getRecordID(), vendorAddress);
			String fJSONString = vendorAddressHistory.toJsonString(true);
			VendorAddressHistory vendorAddressHistory1 = VendorAddressHistory.newInstance(fJSONString);

			if (vendorAddressHistory.toJsonString().equals(vendorAddressHistory1.toJsonString()))
				System.out.println("Both the object instances have same properties value in JSON string");
			else
				System.err.println("Both the object instances do not have same properties value in JSON string");

			VendorAddress lastVendorAddress = (VendorAddress) vendorAddress.clone();
			System.out.println("Value of Address1 in Cloned VendorAddress is " + vendorAddress.getRecordID() + " is "
					+ lastVendorAddress.getAddress1());

			String oldAddress1 = vendorAddress.getAddress1();
			vendorAddress.setAddress1("New Address 1");

			VendorAddressManager.updateVendorAddress(vendorAddress, lastVendorAddress, UsersID.ADMIN);
			System.out.println(
					"Updated Address1 for VendorAddress ID " + vendorAddress.getRecordID() + " is " + vendorAddress.getAddress1());

			//Now revert that prior update
			System.out.println("Restoring Address1 to " + oldAddress1);
			vendorAddress.setAddress1(oldAddress1);
			vendorAddress.saveOrUpdate(UsersID.ADMIN);
		}

		finally
		{
			SystemInitializer.shutdown();
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}