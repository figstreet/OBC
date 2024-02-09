package com.figstreet.test;

import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.productrating.ProductRating;
import com.figstreet.data.productrating.ProductRatingID;
import com.figstreet.data.productrating.ProductRatingList;
import com.figstreet.data.users.UsersID;

public class TestProductRating
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

			ProductRatingID test = new ProductRatingID("3");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			ProductRating testProductRating = ProductRating.getByProductRatingID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testProductRating.getRating());

			ProductRating productRating = new ProductRating(new ProductID(3), new UsersID(105), 2.5, UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			productRating.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + productRating.getRecordID());
			trans.commit();
			trans.close();

			startMS = System.currentTimeMillis();
			productRating.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + productRating.getRecordID());

			startMS = System.currentTimeMillis();
			productRating.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			productRating = new ProductRating(new ProductID(4), new UsersID(104), 5.5, UsersID.ADMIN);
			productRating.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + productRating.getRecordID());
			productRating.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + productRating.getRecordID());
			trans.commit();
			//trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");

			ProductID productID = new ProductID(4);
			ProductRatingList ratingList = ProductRatingList.loadByProductID(productID);
			System.out.println("Number ratings for ProductID " + productID + ": " + ratingList.size());


			List<ProductRatingID> idList = ProductRatingList.loadProductRatingIDList(new ProductRatingID("5"), 2);
			System.out.println("Prids: " + idList.size());
			for (ProductRatingID prid : idList)
				System.out.println(prid);
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
