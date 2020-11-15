package com.figstreet.data.history;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendoraddress.VendorAddressID;
import com.figstreet.data.vendorcontact.VendorContactID;

public class HistoryList extends HibernateList<History>
{
	private static final long serialVersionUID = 1L;

	public static HistoryList loadByVendorOrProductID(VendorID pVendorID, ProductID pProductID, String pType)
			throws SQLException
	{
		if (pVendorID == null && pProductID == null)
			throw new IllegalArgumentException("Either VendorID or ProductID must be specified");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(3);
		StringBuilder query = new StringBuilder();
		boolean needAnd = false;
		if (pVendorID != null)
		{
			if (needAnd)
				query.append(" AND ");
			query.append(History.VENDORID_COLUMN);
			query.append(" = :");
			query.append(History.VENDORID_COLUMN);
			args.put(History.VENDORID_COLUMN, pVendorID);
			needAnd = true;
		}

		if (pProductID != null)
		{
			if (needAnd)
				query.append(" AND ");
			query.append(History.PRODUCTID_COLUMN);
			query.append(" = :");
			query.append(History.PRODUCTID_COLUMN);
			args.put(History.PRODUCTID_COLUMN, pProductID);
			needAnd = true;
		}

		if (pType != null)
		{
			if (needAnd)
				query.append(" AND ");
			query.append(History.TYPE_COLUMN);
			query.append(" = :");
			query.append(History.TYPE_COLUMN);
			args.put(History.TYPE_COLUMN, pType);
			needAnd = true;
		}
		return History.DB_CONNECTOR.loadList(query.toString(), args, "ORDER BY " + History.ID_COLUMN + " ASC", -1);
	}

	public static HistoryList loadByVendorAddressID(VendorAddressID pVendorAddressID, String pType) throws SQLException
	{
		if (pVendorAddressID == null)
			throw new IllegalArgumentException("VendorAddressID must be specified");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
		StringBuilder query = new StringBuilder(History.VENDORADDRESSID_COLUMN);
		query.append(" = :");
		query.append(History.VENDORADDRESSID_COLUMN);
		args.put(History.VENDORADDRESSID_COLUMN, pVendorAddressID);

		if (pType != null)
		{
			query.append(" AND ");
			query.append(History.TYPE_COLUMN);
			query.append(" = :");
			query.append(History.TYPE_COLUMN);
			args.put(History.TYPE_COLUMN, pType);
		}

		return History.DB_CONNECTOR.loadList(query.toString(), args, "ORDER BY " + History.ID_COLUMN + " ASC", -1);
	}

	public static HistoryList loadByVendorContactID(VendorContactID pVendorContactID, String pType) throws SQLException
	{
		if (pVendorContactID == null)
			throw new IllegalArgumentException("VendorContactID must be specified");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
		StringBuilder query = new StringBuilder(History.VENDORCONTACTID_COLUMN);
		query.append(" = :");
		query.append(History.VENDORCONTACTID_COLUMN);
		args.put(History.VENDORCONTACTID_COLUMN, pVendorContactID);

		if (pType != null)
		{
			query.append(" AND ");
			query.append(History.TYPE_COLUMN);
			query.append(" = :");
			query.append(History.TYPE_COLUMN);
			args.put(History.TYPE_COLUMN, pType);
		}
		return History.DB_CONNECTOR.loadList(query.toString(), args, "ORDER BY " + History.ID_COLUMN + " ASC", -1);
	}
}
