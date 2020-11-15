package com.figstreet.data.product;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.figstreet.core.HibernateList;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorproduct.VendorProduct;

public class ProductList extends HibernateList<Product>
{
	private static final long serialVersionUID = 7946162665371139910L;

	public LinkedHashMap<String, ProductList> buildMapByUPC()
	{
		LinkedHashMap<String, ProductList> upcMap = new LinkedHashMap<>(this.size());
		for (Product product : this)
		{
			ProductList byUpc = upcMap.get(product.getUpc());
			if (byUpc == null)
			{
				byUpc = new ProductList();
				upcMap.put(product.getUpc(), byUpc);
			}
			byUpc.add(product);
		}
		return upcMap;
	}

	public Map<ProductID, Product> buildMapByProductID()
	{
		LinkedHashMap<ProductID, Product> map = new LinkedHashMap<>(this.size());
		for (Product product : this)
			map.put(product.getRecordID(), product);
		return map;
	}

	public static ProductList loadByUpc(String pUpc) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(Product.UPC_COLUMN);
		query.append(" = :");
		query.append(Product.UPC_COLUMN);
		args.put(Product.UPC_COLUMN, pUpc);
		return Product.DB_CONNECTOR.loadList(query.toString(), args, getOrderBy(Product.ID_COLUMN + " ASC"), -1);
	}

	public static ProductList loadByUpcList(List<String> pUpcCodes) throws SQLException
	{
		if (pUpcCodes == null || pUpcCodes.isEmpty())
			throw new IllegalArgumentException("UpcCodes List cannot be empty");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(Product.UPC_COLUMN);
		query.append(" in (:");
		query.append(Product.UPC_COLUMN);
		query.append(")");
		args.put(Product.UPC_COLUMN, pUpcCodes);
		return Product.DB_CONNECTOR.loadList(query.toString(), args, getOrderBy(Product.ID_COLUMN + " ASC"), -1);
	}

	public static ProductList loadProductList(ProductID pGreaterThan, int pLimit) throws SQLException
	{
		LinkedHashMap<String, Object> args = null;
		StringBuilder whereClause = new StringBuilder();
		if (pGreaterThan != null)
		{
			args = new LinkedHashMap<>(1);
			whereClause.append(Product.ID_COLUMN);
			whereClause.append(" > :");
			whereClause.append(Product.ID_COLUMN);
			args.put(Product.ID_COLUMN, pGreaterThan);
		}

		return Product.DB_CONNECTOR.loadList(whereClause.toString(), args, getOrderBy(Product.ID_COLUMN + " ASC"),
				pLimit);
	}

	public static List<ProductID> loadProductIDList(ProductID pGreaterThan, int pLimit) throws SQLException
	{
		LinkedHashMap<String, Object> args = null;
		StringBuilder whereClause = new StringBuilder();
		if (pGreaterThan != null)
		{
			args = new LinkedHashMap<>(1);
			whereClause.append(Product.ID_COLUMN);
			whereClause.append(" > :");
			whereClause.append(Product.ID_COLUMN);
			args.put(Product.ID_COLUMN, pGreaterThan);
		}

		return Product.DB_CONNECTOR.loadPkList(whereClause.toString(), args, getOrderBy(Product.ID_COLUMN + " ASC"),
				pLimit);
	}

	public static List<ProductID> loadMissingFromVendor(VendorID pMissingVendorID, int pLimit) throws SQLException
	{
		LinkedHashMap<String, Object> args = null;
		StringBuilder whereClause = new StringBuilder();
		args = new LinkedHashMap<>(1);
		whereClause.append(Product.ID_COLUMN);
		whereClause.append(" NOT IN (SELECT ");
		whereClause.append(VendorProduct.PRODUCTID_COLUMN);
		whereClause.append(" FROM ");
		whereClause.append("vendor_product");//TODO get table name from hibernate
		whereClause.append(" WHERE ");
		whereClause.append(VendorProduct.VENDORID_COLUMN);
		whereClause.append(" = :");
		whereClause.append(VendorProduct.VENDORID_COLUMN);
		args.put(VendorProduct.VENDORID_COLUMN, pMissingVendorID);
		whereClause.append(") ");

		return Product.DB_CONNECTOR.loadPkList(whereClause.toString(), args, getOrderBy(Product.ID_COLUMN + " ASC"),
				pLimit);
	}

	public static ProductList loadByProductIDs(Set<ProductID> pProductIDs) throws SQLException
	{
		if (pProductIDs == null || pProductIDs.isEmpty())
			throw new IllegalArgumentException("pProductIDs cannot be empty");

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(Product.ID_COLUMN);
		query.append(" in (:");
		query.append(Product.ID_COLUMN);
		query.append(")");
		args.put(Product.ID_COLUMN, pProductIDs);
		return Product.DB_CONNECTOR.loadList(query.toString(), args, getOrderBy(Product.ID_COLUMN + " ASC"), -1);
	}
}
