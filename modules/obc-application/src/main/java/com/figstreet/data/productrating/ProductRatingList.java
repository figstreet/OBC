package com.figstreet.data.productrating;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import com.figstreet.core.HibernateList;
import com.figstreet.data.product.ProductID;

public class ProductRatingList extends HibernateList<ProductRating>
{
	private static final long serialVersionUID = 1180720031032700719L;

	public static ProductRatingList loadByProductID(ProductID pProductID) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(ProductRating.PRODUCTID_COLUMN);
		query.append(" = :");
		query.append(ProductRating.PRODUCTID_COLUMN);
		args.put(ProductRating.PRODUCTID_COLUMN, pProductID);
		return ProductRating.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + ProductRating.ID_COLUMN + " ASC", -1);
	}

	public static List<ProductRatingID> loadProductRatingIDList(ProductRatingID pGreaterThan, int pLimit)
			throws SQLException
	{
		LinkedHashMap<String, Object> args = null;
		StringBuilder whereClause = new StringBuilder();
		if (pGreaterThan != null)
		{
			args = new LinkedHashMap<>(1);
			whereClause.append(ProductRating.ID_COLUMN);
			whereClause.append(" > :");
			whereClause.append(ProductRating.ID_COLUMN);
			args.put(ProductRating.ID_COLUMN, pGreaterThan);
		}
		return ProductRating.DB_CONNECTOR.loadPkList(whereClause.toString(), args,
				getOrderBy(ProductRating.ID_COLUMN + " ASC"), pLimit);
	}
}
