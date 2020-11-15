package com.figstreet.test;

import com.figstreet.biz.vendorproduct.VendorProductHistory;
import com.figstreet.biz.vendorproduct.VendorProductManager;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.figstreet.data.vendorproduct.VendorProductID;

public class TestVendorProductHistory
{

	public static void main(String[] args) throws Exception
	{
		double fPrice;
		AmazonMarketplace fAmazonMarketplace;
		PriceCurrency fPriceCurrency;
		
		try
		{
			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

			VendorProductID test = new VendorProductID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();

			VendorProduct vendorProduct = VendorProduct.getByVendorProductID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Price in VendorProduct " + test.getValue() + " is " + vendorProduct.getPrice());

			VendorProductHistory vendorProductHistory = new VendorProductHistory(test, vendorProduct);
			String fJSONString = vendorProductHistory.toJsonString(true);
			VendorProductHistory vendorProductHistory1 = VendorProductHistory.newInstance(fJSONString);

			if (vendorProductHistory.toJsonString().equals(vendorProductHistory1.toJsonString()))
				System.out.println("Both the object instances have same properties value in JSON string");
			else
				System.out.println("Both the object instances do not have same properties value in JSON string");

			VendorProduct lastVendorProduct = (VendorProduct) vendorProduct.clone();
			System.out.println("Value of Price in Cloned VendorProduct is "  + test.getValue() + " is "+ lastVendorProduct.getPrice());

			
			fAmazonMarketplace = AmazonMarketplace.UNITED_STATES; 
			vendorProduct.setAmazonMarketplace(fAmazonMarketplace);
			fPriceCurrency = PriceCurrency.USD; 
			vendorProduct.setPriceCurrency(fPriceCurrency);
			
			//fPrice = 24.0;
			//vendorProduct.setPrice(fPrice);
			trans.commit();
			trans.close();

			VendorProductManager.updateVendorProduct(vendorProduct, lastVendorProduct, UsersID.ADMIN);
			System.out.println(
					"Updated Price for VendorProduct ID " + test.getValue() + " is " + vendorProduct.getPrice());
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