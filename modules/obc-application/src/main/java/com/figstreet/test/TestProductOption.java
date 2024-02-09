package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.productoption.ProductOption;
import com.figstreet.data.productoption.ProductOptionID;
import com.figstreet.data.productoption.ProductOptionList;
import com.figstreet.data.users.UsersID;

public class TestProductOption
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

			ProductOptionID test = new ProductOptionID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			ProductOption testProductOption = ProductOption.getBypProductOptionID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testProductOption.getSimilarProductID());
			trans.commit();
			trans.close();


			ProductOption productOption = new ProductOption(new ProductID(2), new ProductID(5), UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			productOption.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + productOption.getRecordID());

			startMS = System.currentTimeMillis();
			productOption.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + productOption.getRecordID());

			startMS = System.currentTimeMillis();
			productOption.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			productOption = new ProductOption(new ProductID(2), new ProductID(7), UsersID.ADMIN);
			productOption.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + productOption.getRecordID());
			productOption.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + productOption.getRecordID());
			trans.commit();
			//trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");

			ProductID productID = new ProductID(2);
			ProductOptionList product2Options = ProductOptionList.loadByProductID(productID);
			System.out.println("Found " + product2Options.size() + " options for ProductID " + productID);

			/*
			 * startMS = System.currentTimeMillis(); ProductOptionList allProductOption =
			 * ProductOptionList.loadAll(-1, null); endMS = System.currentTimeMillis();
			 * System.out.println("Loaded all product options in " + (endMS - startMS) + " ms " +
			 * "\nSize of product in DB " + allProductOption.size());
			 */
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
