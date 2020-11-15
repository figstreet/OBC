package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.amazonpricing.AmazonPricingID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;

public class TestAmazonPricing
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

			AmazonPricingID test = new AmazonPricingID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			AmazonPricing testAmazonPricing = AmazonPricing.getByAmazonPricingID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testAmazonPricing.getOfferCount());

			AmazonPricing amazonPricing = new AmazonPricing(new VendorProductID((long) 1), UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			amazonPricing.saveOrUpdate(UsersID.ADMIN); // should retrieve existing transaction
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + amazonPricing.getRecordID());
			trans.commit();
			trans.close();

			startMS = System.currentTimeMillis();
			amazonPricing.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + amazonPricing.getRecordID());

			startMS = System.currentTimeMillis();
			amazonPricing.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			amazonPricing = new AmazonPricing(new VendorProductID((long) 4), UsersID.ADMIN);
			amazonPricing.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + amazonPricing.getRecordID());
			amazonPricing.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + amazonPricing.getRecordID());
			trans.commit();
			// trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");
		} finally
		{
			SystemInitializer.shutdown();
		}
	}
}