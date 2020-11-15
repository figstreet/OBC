package com.figstreet.biz.product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.product.Product;
import com.figstreet.data.users.UsersID;

public class ProductManager
{
	public static List<String> updateProduct(Product pProduct, Product pLastProduct, UsersID pBy) throws SQLException
	{
		List<String> messages = new ArrayList<>();
		if (pProduct == null)
			messages.add("Product cannot be null");

		//TODO - check permissions for updating a product

		if (messages.isEmpty())
		{
			History productHistory = ProductHistory.buildProductHistory(pProduct, pLastProduct, pBy);
			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				pProduct.saveOrUpdate(pBy);
				if (productHistory != null)
					productHistory.saveOrUpdate(pBy);
				trans.commit();
			}
			finally
			{
				trans.close();
			}
		}
		return messages;
	}
}
