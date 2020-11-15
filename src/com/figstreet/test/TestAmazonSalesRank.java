package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.amazonsalesrank.AmazonSalesRankID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;

public class TestAmazonSalesRank
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

			AmazonSalesRankID test = new AmazonSalesRankID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			AmazonSalesRank testAmazonSalesRank = AmazonSalesRank.getByAmazonSalesRankID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testAmazonSalesRank.getPrimaryRank());

			AmazonSalesRank amazonSalesRank = new AmazonSalesRank(new VendorProductID((long) 2), UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			amazonSalesRank.saveOrUpdate(UsersID.ADMIN); // should retrieve existing transaction
			endMS = System.currentTimeMillis();
			System.out
					.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + amazonSalesRank.getRecordID());
			trans.commit();
			trans.close();

			startMS = System.currentTimeMillis();
			amazonSalesRank.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out
					.println("Updated in " + (endMS - startMS) + " ms with RecordID " + amazonSalesRank.getRecordID());

			startMS = System.currentTimeMillis();
			amazonSalesRank.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			amazonSalesRank = new AmazonSalesRank(new VendorProductID((long) 4), UsersID.ADMIN);
			amazonSalesRank.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + amazonSalesRank.getRecordID());
			amazonSalesRank.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + amazonSalesRank.getRecordID());
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