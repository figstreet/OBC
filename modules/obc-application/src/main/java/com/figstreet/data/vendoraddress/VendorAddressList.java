package com.figstreet.data.vendoraddress;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;

public class VendorAddressList extends HibernateList<VendorAddress>
{

	private static final long serialVersionUID = -7820616822277489381L;

	public static VendorAddressList loadByVendorID(String pVendorID) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(VendorAddress.VENDORID_COLUMN);
		query.append(" = :");
		query.append(VendorAddress.VENDORID_COLUMN);
		args.put(VendorAddress.VENDORID_COLUMN, pVendorID);
		return VendorAddress.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + VendorAddress.ID_COLUMN + " ASC", -1);
	}

}
