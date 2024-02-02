package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.userpermission.UserPermission;
import com.figstreet.data.userpermission.UserPermissionID;
import com.figstreet.data.userpermission.UserPermissionList;
import com.figstreet.data.users.UsersID;


public class TestUserPermission
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

			UserPermissionID test = new UserPermissionID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			UserPermission testUserPermission = UserPermission.getByUserPermissionID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testUserPermission.getType());

			UserPermissionList upList = UserPermissionList.loadByUsersID(UsersID.ADMIN);
			System.out.println("Found " + upList.size() + " permissions for " + UsersID.asString(UsersID.ADMIN));

			trans.commit();
			trans.close();
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}
}
