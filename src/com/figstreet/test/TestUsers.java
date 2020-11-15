package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.users.Users;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.users.UsersList;

public class TestUsers
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

			UsersList usersList = UsersList.loadByEmail("jonathan@figstreet.com");
			System.out.println("Matching email ID found in database: " + usersList.size());

			UsersID test = new UsersID("100");
			startMS = System.currentTimeMillis();
			Users testUsers = Users.getByUsersID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testUsers.getEmail());

			Users user = new Users("124@figstreet.com", "FIGSTREET124_USER", "FNAME124", UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			user.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + user.getRecordID());

			startMS = System.currentTimeMillis();
			user.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + user.getRecordID());

			startMS = System.currentTimeMillis();
			user.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			user = new Users("125@figstreet.com", "FIGSTREET125_USER", "FNAME125", UsersID.ADMIN);
			user.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + user.getRecordID());
			user.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + user.getRecordID());
			//trans.commit();
			trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");
		} finally
		{
			SystemInitializer.shutdown();
		}

	}

}
