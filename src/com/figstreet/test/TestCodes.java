package com.figstreet.test;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.RefreshCacheManager;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.codes.CodeCache;
import com.figstreet.data.codes.Codes;
import com.figstreet.data.codes.CodesID;
import com.figstreet.data.codes.CodesList;
import com.figstreet.data.codes.CodesType;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendoraddress.VendorAddressType;

public class TestCodes
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		try
		{
			System.out.println(CodesType.class.getName());

			System.out.println("VAT size pre-initialize: " + VendorAddressType.getAllValues(false).size());

			long startMS = System.currentTimeMillis();
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
			long endMS = System.currentTimeMillis();
			System.out.println("Initialize in : " + (endMS - startMS));

//			System.out.println("VAT size post-initialize: " + VendorAddressType.getAllValues(false).size());
//
//			List<CodesType> codesTypeList = CodesType.getAllValues();
//			System.out.println("CodesType count " + codesTypeList.size());
//			for (CodesType type : codesTypeList)
//				System.out.println("\t" + type.toString() + " : " + type.getDisplay());

			CodesID test = new CodesID("1");
			startMS = System.currentTimeMillis();
			HibernateTransaction trans = HibernateTransaction.open();
			Codes testCode = Codes.getByCodesID(test);
			endMS = System.currentTimeMillis();
			System.out.println("Load in " + (endMS - startMS) + " ms " + testCode.getDescription());
			testCode.saveOrUpdate(UsersID.ADMIN);
			trans.rollback();
			trans.close();

//			Codes masterVAT = new Codes(CodesType.MASTER_CODES.toString(), "VAT", "Vendor Address Type", UsersID.ADMIN);
//			startMS = System.currentTimeMillis();
//			masterVAT.saveOrUpdate(null, UsersID.ADMIN);
//			endMS = System.currentTimeMillis();
//			System.out.println("Inserted in " + (endMS - startMS) + " ms with RecordID " + masterVAT.getRecordID());
//
//			Codes vat1 = new Codes(CodesType.VENDOR_ADDRESS_TYPE.toString(), "MAILING", "Mailing Address", UsersID.ADMIN);
//			vat1.saveOrUpdate(null,  UsersID.ADMIN);
//			Codes vat2 = new Codes(CodesType.VENDOR_ADDRESS_TYPE.toString(), "WAREHOUSE", "Warehouse Address", UsersID.ADMIN);
//			vat2.saveOrUpdate(null,  UsersID.ADMIN);
//			Codes vat3 = new Codes(CodesType.VENDOR_ADDRESS_TYPE.toString(), "STORE", "Store Address", UsersID.ADMIN);
//			vat3.saveOrUpdate(null,  UsersID.ADMIN);
//
//			startMS = System.currentTimeMillis();
//			userInactiveReason.saveOrUpdate(null, UsersID.ADMIN);
//			endMS = System.currentTimeMillis();
//			System.out.println("Updated in " + (endMS - startMS) + " ms with RecordID" + userInactiveReason.getRecordID());
//
//			startMS = System.currentTimeMillis();
//			userInactiveReason.delete(null);
//			endMS = System.currentTimeMillis();
//			System.out.println("Deleted in " + (endMS - startMS) + " ms ");
//
//			startMS = System.currentTimeMillis();
//			trans = HibernateTransaction.open();
//			userInactiveReason = new Codes("00", "UIR", "User Inactive Reasons", UsersID.ADMIN);
//			userInactiveReason.saveOrUpdate(trans, UsersID.ADMIN);
//			System.out.println("Inserted: " + userInactiveReason.getRecordID());
//			userInactiveReason.saveOrUpdate(trans, UsersID.ADMIN);
//			System.out.println("Updated: " + userInactiveReason.getRecordID());
//			trans.commit();
//			trans.rollback();
//			trans.close();
//			endMS = System.currentTimeMillis();
//			System.out.println("Insert, update, rollback in " + (endMS - startMS) + " ms ");

			startMS = System.currentTimeMillis();
			CodesList allCodes = CodesList.loadAll(-1);
			endMS = System.currentTimeMillis();
			System.out.println(
					"Loaded all codes in " + (endMS - startMS) + " ms " + "\nSize of codes in DB " + allCodes.size());

			startMS = System.currentTimeMillis();
			CodesList masterCodes = CodesList.loadByType(CodesType.MASTER_CODES);
			endMS = System.currentTimeMillis();
			System.out.println(
					"Loaded 00 codes in " + (endMS - startMS) + " ms " + "\nSize of codes in DB " + masterCodes.size());

			Collection<VendorAddressType> vatCollection = VendorAddressType.getAllValues(false);
			System.out.println("Found " + vatCollection.size() + " VAT codes");
			for (VendorAddressType vat : vatCollection)
				System.out.println("\t" + vat.toString() + " : " + vat.getDisplay());

			RefreshCacheManager.getThe().scheduleRefresh(CodeCache.getThe(), 1, true, UsersID.ADMIN);
			TimeUnit.SECONDS.sleep(2);

			vatCollection = VendorAddressType.getAllValues(true);
			System.out.println("Found " + vatCollection.size() + " VAT codes in DB and Java");
			for (VendorAddressType vat : vatCollection)
				System.out.println("\t" + vat.toString() + " : " + vat.getDisplay());

		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

}
