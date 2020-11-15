package com.figstreet.biz.amazonpricing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;

public class AmazonPricingManager
{

	public static List<String> updateAmazonPricing(AmazonPricing pAmazonPricing, AmazonPricing pLastAmazonPricing,
			UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pAmazonPricing == null)
			messages.add("AmazonPricing cannot be null");

		// TODO - check permissions for updating a product

		if (messages.isEmpty())
		{
			History amazonPricingHistory = AmazonPricingHistory.buildAmazonPricingHistory(pAmazonPricing,
					pLastAmazonPricing, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pAmazonPricing.saveOrUpdate(pBy);
				if (amazonPricingHistory != null)
					amazonPricingHistory.saveOrUpdate(pBy);
				trans.commit();
			} finally
			{
				trans.close();
			}
		}
		return messages;
	}

}