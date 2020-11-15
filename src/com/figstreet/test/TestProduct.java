package com.figstreet.test;

import java.util.List;

import com.figstreet.biz.product.ProductHistory;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.product.ProductList;

public class TestProduct
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

			ProductID test = new ProductID("2");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			Product testProduct = Product.getBypProductID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testProduct.getName());
			trans.commit();
			trans.close();

//			String upc = "630509512638";
//			ProductList upcProductList = ProductList.loadByUpc(upc, null);
//			System.out.println("Found " + upcProductList.size() + " Products with UPC " + upc);
//
//			ArrayList<String> upcCodes = new ArrayList<>(2);
//			upcCodes.add(upc);
//			upcCodes.add("3558380053231");
//
//			upcProductList = ProductList.loadByUpcList(upcCodes, null);
//			System.out.println("Found " + upcProductList.size() + " Products with UPCs");

			ProductHistory history = new ProductHistory(test, testProduct);
			String productHistoryJson = history.toJsonString(true);
			System.out.println(productHistoryJson);

			ProductHistory fromJson = ProductHistory.newInstance(productHistoryJson);
			System.out.println(fromJson.toJsonString(true));

			ProductID startID = new ProductID("100");
			List<ProductID> idList = ProductList.loadProductIDList(startID, 200);
			while (!idList.isEmpty())
			{
				ProductID endID = idList.get(idList.size()-1);
				System.out.println("Prids: " + idList.size());
				System.out.println(" Start: " + ProductID.asString(idList.get(0)) + " End: " + ProductID.asString(endID));
				startID = endID;
				idList = ProductList.loadProductIDList(startID, 200);
			}
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
