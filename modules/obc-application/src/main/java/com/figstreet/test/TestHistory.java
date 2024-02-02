package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.history.History;
import com.figstreet.data.history.HistoryID;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;

public class TestHistory
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

			HistoryID test = new HistoryID("2");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			History testHistory = History.getByHistoryID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testHistory.getType());
			trans.commit();
			trans.close();

			History historyrec = new History(new ProductID(2), new VendorID(1), UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			historyrec.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + historyrec.getRecordID());

			startMS = System.currentTimeMillis();
			historyrec.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID" + historyrec.getRecordID());

			startMS = System.currentTimeMillis();
			historyrec.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			historyrec = new History(new ProductID(2), new VendorID(1), UsersID.ADMIN);
			historyrec.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + historyrec.getRecordID());
			historyrec.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + historyrec.getRecordID());
			trans.commit();
			trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");

		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
