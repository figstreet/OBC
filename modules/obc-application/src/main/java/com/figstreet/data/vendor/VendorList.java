package com.figstreet.data.vendor;

import java.sql.SQLException;

import com.figstreet.core.HibernateList;

public class VendorList extends HibernateList<Vendor>
{
	private static final long serialVersionUID = 3680049329029615195L;

	public static VendorList loadAll(int pLimit) throws SQLException
	{
		return Vendor.DB_CONNECTOR.loadList(null, null, "ORDER BY " + Vendor.ID_COLUMN + " ASC", pLimit);
	}

}
