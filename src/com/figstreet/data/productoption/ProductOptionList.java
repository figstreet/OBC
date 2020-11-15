package com.figstreet.data.productoption;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;
import com.figstreet.data.product.ProductID;

public class ProductOptionList extends HibernateList<ProductOption>
{
	private static final long serialVersionUID = 4041363780910056879L;

	public static ProductOptionList loadByProductID(ProductID pProductID) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(ProductOption.PRODUCTID_COLUMN);
		query.append(" = :");
		query.append(ProductOption.PRODUCTID_COLUMN);
		args.put(ProductOption.PRODUCTID_COLUMN, pProductID);
		return ProductOption.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + ProductOption.PRICE_DIFFERENCE_COLUMN + " ASC", -1);
	}

}
