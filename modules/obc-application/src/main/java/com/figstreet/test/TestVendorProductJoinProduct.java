package com.figstreet.test;

import com.figstreet.core.SystemInitializer;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.vendor.VendorID;

public class TestVendorProductJoinProduct
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

			VendorID testVendorID = new VendorID("1");
			startMS = System.currentTimeMillis();
			//VendorProductJoinProductList vpjpList = VendorProductJoinProductList.loadList(testVendorID, null, 10);
			//System.out.println(vpjpList.size());

			String test = "toy_display_on_website";
			AmazonSalesRankCategory category = AmazonSalesRankCategory.newInstance(test);
			System.out.println(category.getDisplay());

			test = "251910011";
			category = AmazonSalesRankCategory.newInstance(test);
			System.out.println(category.getDisplay());
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
