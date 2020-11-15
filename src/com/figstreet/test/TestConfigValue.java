package com.figstreet.test;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.configvalue.ConfigValue;
import com.figstreet.data.configvalue.ConfigValueID;
import com.figstreet.data.configvalue.ConfigValueList;
import com.figstreet.data.users.UsersID;

public class TestConfigValue
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

			ConfigValueID test = new ConfigValueID("3");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			ConfigValue testConfigValue = ConfigValue.getByConfigValueID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testConfigValue.getConfigValueType());

			ConfigValue configType = new ConfigValue("GENERAL", "TEST1", "USA", UsersID.ADMIN);
			startMS = System.currentTimeMillis();
			configType.saveOrUpdate(UsersID.ADMIN); //should retrieve existing transaction
			endMS = System.currentTimeMillis();
			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + configType.getRecordID());
			trans.commit();
			trans.close();

			startMS = System.currentTimeMillis();
			configType.saveOrUpdate(UsersID.ADMIN);
			endMS = System.currentTimeMillis();
			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID " + configType.getRecordID());

			startMS = System.currentTimeMillis();
			configType.delete();
			endMS = System.currentTimeMillis();
			System.out.println("Deleted in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			trans = HibernateTransaction.open();
			configType = new ConfigValue("GENERAL", "TEST", "USA", UsersID.ADMIN);
			configType.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Inserted: " + configType.getRecordID());
			configType.saveOrUpdate(UsersID.ADMIN);
			System.out.println("Updated: " + configType.getRecordID());
			//trans.commit();
			trans.rollback();
			trans.close();
			endMS = System.currentTimeMillis();
			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			ConfigValueList allConfigValues = ConfigValueList.loadAll(-1);
			endMS = System.currentTimeMillis();
			System.out.println("Loaded all configvalues in " + (endMS - startMS) + " ms " + "\nSize of configvalues in DB " + allConfigValues.size());
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
