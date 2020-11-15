package com.figstreet.data.vendorcontact;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;

public class VendorContactList extends HibernateList<VendorContact>
{
	private static final long serialVersionUID = 6804906846501183036L;

	public static VendorContactList loadByVendorID(String pVendorID) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(VendorContact.VENDORID_COLUMN);
		query.append(" = :");
		query.append(VendorContact.VENDORID_COLUMN);
		args.put(VendorContact.VENDORID_COLUMN, pVendorID);
		return VendorContact.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + VendorContact.ID_COLUMN + " ASC", -1);
	}

}
