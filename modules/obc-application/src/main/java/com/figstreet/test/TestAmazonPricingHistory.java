package com.figstreet.test;

import com.figstreet.biz.amazonpricing.AmazonPricingHistory;
import com.figstreet.biz.amazonpricing.AmazonPricingManager;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.amazonpricing.AmazonPricingID;
import com.figstreet.data.users.UsersID;

public class TestAmazonPricingHistory
{

	public static void main(String[] args) throws Exception
	{

		Double fSecondaryShippingPrice;

		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			AmazonPricingID test = new AmazonPricingID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();

			AmazonPricing amazonPricing = AmazonPricing.getByAmazonPricingID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Buybox Item Price in AmazonPricing " + test.getValue() + " is "
					+ amazonPricing.getBuyboxItemPrice());

			AmazonPricingHistory amazonPricingHistory = new AmazonPricingHistory(test, amazonPricing);
			String fJSONString = amazonPricingHistory.toJsonString(true);
			AmazonPricingHistory amazonPricingHistory1 = AmazonPricingHistory.newInstance(fJSONString);

			if (amazonPricingHistory.toJsonString().equals(amazonPricingHistory1.toJsonString()))
				System.out.println("Both the object instances have same properties value in JSON string");
			else
				System.out.println("Both the object instances do not have same properties value in JSON string");

			AmazonPricing lastAmazonPricing = (AmazonPricing) amazonPricing.clone();
			System.out.println("Value of Secondary Shipping Price for AmazonPricing ID " + test.getValue() + " is "
					+ lastAmazonPricing.getSecondaryShippingPrice());

			fSecondaryShippingPrice = 15.0;
			amazonPricing.setSecondaryShippingPrice(fSecondaryShippingPrice);

			trans.commit();
			trans.close();

			AmazonPricingManager.updateAmazonPricing(amazonPricing, lastAmazonPricing, UsersID.ADMIN);
			System.out.println("Updated Secondary Shipping Price for AmazonPricing ID " + test.getValue() + " is "
					+ amazonPricing.getSecondaryShippingPrice());
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