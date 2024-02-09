package com.figstreet.data.amazonsalesrank;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import com.figstreet.core.HibernateList;
import com.figstreet.data.vendorproduct.VendorProductID;

public class AmazonSalesRankList extends HibernateList<AmazonSalesRank>
{
	private static final long serialVersionUID = 3060914966819207618L;

	public static AmazonSalesRankList loadByVendorProductIDList(List<VendorProductID> pVendorProductIDList)
			throws SQLException
	{
		if (pVendorProductIDList == null || pVendorProductIDList.isEmpty())
			throw new IllegalArgumentException("pVendorProductIDList cannot be empty");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(AmazonSalesRank.VENDORPRODUCTID_COLUMN);
		query.append(" in (:");
		query.append(AmazonSalesRank.VENDORPRODUCTID_COLUMN);
		query.append(")");
		args.put(AmazonSalesRank.VENDORPRODUCTID_COLUMN, pVendorProductIDList);
		return AmazonSalesRank.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + AmazonSalesRank.ID_COLUMN + " ASC", -1);
	}
}