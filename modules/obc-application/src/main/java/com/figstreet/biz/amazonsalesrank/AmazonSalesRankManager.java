package com.figstreet.biz.amazonsalesrank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;

public class AmazonSalesRankManager
{

	public static List<String> updateAmazonSalesRank(AmazonSalesRank pAmazonSalesRank, AmazonSalesRank pLastAmazonSalesRank,
			UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pAmazonSalesRank == null)
			messages.add("AmazonSalesRank cannot be null");

		// TODO - check permissions for updating a product

		if (messages.isEmpty())
		{
			History amazonSalesRankHistory = AmazonSalesRankHistory.buildAmazonSalesRankHistory(pAmazonSalesRank,
					pLastAmazonSalesRank, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pAmazonSalesRank.saveOrUpdate(pBy);
				if (amazonSalesRankHistory != null)
					amazonSalesRankHistory.saveOrUpdate(pBy);
				trans.commit();
			} finally
			{
				trans.close();
			}
		}
		return messages;
	}

}