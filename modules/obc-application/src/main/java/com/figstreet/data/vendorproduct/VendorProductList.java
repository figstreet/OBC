package com.figstreet.data.vendorproduct;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.figstreet.core.FormatUtil;
import com.figstreet.core.HibernateList;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.vendor.VendorID;

public class VendorProductList extends HibernateList<VendorProduct>
{
	private static final long serialVersionUID = 1017862616687149628L;

	public Set<ProductID> buildProductIDSet()
	{
		LinkedHashSet<ProductID> productIDSet = new LinkedHashSet<>(this.size());
		for (VendorProduct vp : this)
		{
			productIDSet.add(vp.getProductID());
		}
		return productIDSet;
	}

	public Map<String, VendorProduct> buildMapByVendorIdentifier()
	{
		LinkedHashMap<String, VendorProduct> map = new LinkedHashMap<>(this.size());
		for (VendorProduct vp : this)
			map.put(vp.getVendorIdentifier(), vp);
		return map;
	}

	public static VendorProductList loadByVendorAndProductID(VendorID pVendorID, ProductID pProductID)
			throws SQLException
	{
		if (pVendorID != null && pProductID != null)
		{
			LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
			StringBuilder query = new StringBuilder(VendorProduct.VENDORID_COLUMN);

			query.append(" = :");
			query.append(VendorProduct.VENDORID_COLUMN);
			args.put(VendorProduct.VENDORID_COLUMN, pVendorID);

			query.append(" AND " + VendorProduct.PRODUCTID_COLUMN + " = ? ");
			args.put(VendorProduct.PRODUCTID_COLUMN, pProductID);

			return VendorProduct.DB_CONNECTOR.loadList(query.toString(), args,
					"ORDER BY " + VendorProduct.ID_COLUMN + " ASC", -1);
		}
		throw new IllegalArgumentException("Both VendorID and ProductID must be specified");
	}

	public static VendorProductList loadByVendorIdentifierList(VendorID pVendorID, List<String> pVendorIdentifiers)
			throws SQLException
	{
		if (pVendorIdentifiers == null || pVendorIdentifiers.isEmpty())
			throw new IllegalArgumentException("pVendorIdentifiers List cannot be empty");
		if (pVendorID == null)
			throw new IllegalArgumentException("pVendorID is required");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
		StringBuilder query = new StringBuilder(VendorProduct.VENDORID_COLUMN);
		query.append(" = :");
		query.append(VendorProduct.VENDORID_COLUMN);
		args.put(VendorProduct.VENDORID_COLUMN, pVendorID);
		query.append(" AND ");
		query.append(VendorProduct.VENDOR_IDENTIFIER_COLUMN);
		query.append(" in (:");
		query.append(VendorProduct.VENDOR_IDENTIFIER_COLUMN);
		query.append(")");
		args.put(VendorProduct.VENDOR_IDENTIFIER_COLUMN, pVendorIdentifiers);
		return VendorProduct.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + VendorProduct.ID_COLUMN + " ASC", -1);
	}

	public static VendorProductList loadByVendorAndProductIDList(VendorID pVendorID, List<ProductID> pProductIDList)
			throws SQLException
	{
		if (pProductIDList == null || pProductIDList.isEmpty())
			throw new IllegalArgumentException("pProductIDList cannot be empty");
		if (pVendorID == null)
			throw new IllegalArgumentException("pVendorID is required");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
		StringBuilder query = new StringBuilder(VendorProduct.VENDORID_COLUMN);
		query.append(" = :");
		query.append(VendorProduct.VENDORID_COLUMN);
		args.put(VendorProduct.VENDORID_COLUMN, pVendorID);
		query.append(" AND ");
		query.append(VendorProduct.PRODUCTID_COLUMN);
		query.append(" in (:");
		query.append(VendorProduct.PRODUCTID_COLUMN);
		query.append(")");
		args.put(VendorProduct.PRODUCTID_COLUMN, pProductIDList);
		return VendorProduct.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + VendorProduct.ID_COLUMN + " ASC", -1);
	}

	public static VendorProductList loadByVendorID(VendorID pVendorID, VendorProductID pGreaterThan, int pLimit)
			throws SQLException
	{
		if (pVendorID == null)
			throw new IllegalArgumentException("pVendorID is required");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
		StringBuilder query = new StringBuilder(VendorProduct.VENDORID_COLUMN);
		query.append(" = :");
		query.append(VendorProduct.VENDORID_COLUMN);
		args.put(VendorProduct.VENDORID_COLUMN, pVendorID);

		if (pGreaterThan != null)
		{
			query.append(" AND ");
			query.append(VendorProduct.ID_COLUMN);
			query.append(" > :");
			query.append(VendorProduct.ID_COLUMN);
			args.put(VendorProduct.ID_COLUMN, pGreaterThan);
		}
		return VendorProduct.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + VendorProduct.ID_COLUMN + " ASC", pLimit);
	}

	public static int deactiveVendorProducts(VendorID pVendorID, List<VendorProductID> pKeepActiveList) throws SQLException
	{
		if (pVendorID == null)
			throw new IllegalArgumentException("pVendorID is required");
		if (pKeepActiveList == null || pKeepActiveList.isEmpty())
			throw new IllegalArgumentException("pKeepActiveList must have at least one element");

		ArrayList<Object> args = new ArrayList<>();
		StringBuilder update = new StringBuilder("UPDATE ");
		update.append(FormatUtil.escapeSQLServerObject(VendorProduct.TABLE_NAME));
		update.append(" SET ");
		update.append(VendorProduct.ACTIVE_COLUMN);
		update.append(" = ? WHERE ");
		args.add(false);
		update.append(VendorProduct.VENDORID_COLUMN);
		update.append(" = ? AND ");
		args.add(pVendorID);
		update.append(VendorProduct.ID_COLUMN);
		update.append(" > ? AND ");
		args.add(pKeepActiveList.get(0));
		update.append(VendorProduct.ID_COLUMN);
		update.append(" < ? AND ");
		args.add(pKeepActiveList.get(pKeepActiveList.size()-1));
		update.append(VendorProduct.ID_COLUMN);
		update.append(" NOT IN (");
		for (VendorProductID keepActive : pKeepActiveList)
		{
			update.append("?,");
			args.add(keepActive);
		}
		update.replace(update.length()-1, update.length(), ")");


		return VendorProduct.DB_CONNECTOR.updateMany(update.toString(), args);
	}

}
