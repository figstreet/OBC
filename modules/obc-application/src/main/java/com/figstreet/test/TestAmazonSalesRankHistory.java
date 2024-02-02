package com.figstreet.test;

import com.figstreet.biz.amazonsalesrank.AmazonSalesRankHistory;
import com.figstreet.biz.amazonsalesrank.AmazonSalesRankManager;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.amazonsalesrank.AmazonSalesRankID;
import com.figstreet.data.users.UsersID;

public class TestAmazonSalesRankHistory
{
	public static void main(String[] args) throws Exception
	{
		int fPrimaryRank;

		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			AmazonSalesRankID test = new AmazonSalesRankID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();

			AmazonSalesRank amazonSalesRank = AmazonSalesRank.getByAmazonSalesRankID(test);
			endMS = System.currentTimeMillis();
			System.out.println(
					"Primary Rank in AmazonSalesRank " + test.getValue() + " is " + amazonSalesRank.getPrimaryRank());

			AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(test, amazonSalesRank);
			String fJSONString = amazonSalesRankHistory.toJsonString(true);
			AmazonSalesRankHistory amazonSalesRankHistory1 = AmazonSalesRankHistory.newInstance(fJSONString);

			if (amazonSalesRankHistory.toJsonString().equals(amazonSalesRankHistory1.toJsonString()))
				System.out.println("Both the object instances have same properties value in JSON string");
			else
				System.out.println("Both the object instances do not have same properties value in JSON string");

			AmazonSalesRank lastAmazonSalesRank = (AmazonSalesRank) amazonSalesRank.clone();
			System.out.println("Primary Rank for AmazonSalesRank ID " + test.getValue() + " is "
					+ lastAmazonSalesRank.getPrimaryRank());

			fPrimaryRank = 25;
			amazonSalesRank.setPrimaryRank(fPrimaryRank);

			trans.commit();
			trans.close();

			AmazonSalesRankManager.updateAmazonSalesRank(amazonSalesRank, lastAmazonSalesRank, UsersID.ADMIN);
			System.out.println("Updated Primary Rank for AmazonSalesRank ID " + test.getValue() + " is "
					+ amazonSalesRank.getPrimaryRank());
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