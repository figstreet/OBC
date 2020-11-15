package com.figstreet.data.amazonpricing;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import com.figstreet.core.HibernateList;
import com.figstreet.data.vendorproduct.VendorProductID;

public class AmazonPricingList extends HibernateList<AmazonPricing>
{
	private static final long serialVersionUID = -3224611425585559648L;

	public static AmazonPricingList loadByVendorProductIDList(List<VendorProductID> pVendorProductIDList)
			throws SQLException
	{
		if (pVendorProductIDList == null || pVendorProductIDList.isEmpty())
			throw new IllegalArgumentException("pVendorProductIDList cannot be empty");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(AmazonPricing.VENDORPRODUCTID_COLUMN);
		query.append(" in (:");
		query.append(AmazonPricing.VENDORPRODUCTID_COLUMN);
		query.append(")");
		args.put(AmazonPricing.VENDORPRODUCTID_COLUMN, pVendorProductIDList);
		return AmazonPricing.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + AmazonPricing.ID_COLUMN + " ASC", -1);
	}
}