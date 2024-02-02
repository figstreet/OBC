package com.figstreet.test;

import java.util.ArrayList;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.figstreet.data.vendorproduct.VendorProductList;

public class TestVendorProduct
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

			VendorProductID test = new VendorProductID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			VendorProduct testVendorProduct = VendorProduct.getByVendorProductID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testVendorProduct.getPrice());

			VendorProduct vendorProductRec = new VendorProduct(new VendorID(1), new ProductID(2), UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			vendorProductRec.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + vendorProductRec.getRecordID());
			trans.commit();
			trans.close();


			startMS = System.currentTimeMillis();
			vendorProductRec.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID" + vendorProductRec.getRecordID());

			startMS = System.currentTimeMillis();
			vendorProductRec.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			vendorProductRec = new VendorProduct(new VendorID(1), new ProductID(2), UsersID.ADMIN);
			vendorProductRec.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + vendorProductRec.getRecordID());
			vendorProductRec.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + vendorProductRec.getRecordID());
			trans.commit();
			//trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");

			ArrayList<String> identifierList = new ArrayList<>(2);
			identifierList.add("test1");
			identifierList.add("test2");
			VendorProductList byIdentifiers = VendorProductList.loadByVendorIdentifierList(VendorID.EVEREST_TOYS,
					identifierList);
			System.out.println("Found: " + byIdentifiers.size() + " records");
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
