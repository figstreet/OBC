package com.figstreet.data.vendorproduct;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.product.ProductList;
import com.figstreet.data.vendor.VendorID;

public class VendorProductJoinProductList extends ArrayList<VendorProductJoinProduct> implements Serializable
{
	private static final long serialVersionUID = -1418371927397149855L;

	public static VendorProductJoinProductList loadList(VendorID pVendorID, VendorProductID pGreaterThan, int pLimit)
			throws IllegalArgumentException, SQLException
	{
		VendorProductJoinProductList list = new VendorProductJoinProductList();
		VendorProductList vpList = null;
		ProductList productList = null;

		HibernateTransaction trans = HibernateTransaction.open();
		try
		{
			vpList = VendorProductList.loadByVendorID(pVendorID, pGreaterThan, pLimit);
			Set<ProductID> productIDSet = vpList.buildProductIDSet();
			productList = ProductList.loadByProductIDs(productIDSet);
			trans.commit();
		}
		finally
		{
			trans.close();
		}

		Map<ProductID, Product> productMap = productList.buildMapByProductID();
		for (VendorProduct vendorProduct : vpList)
		{
			VendorProductJoinProduct vpjp = new VendorProductJoinProduct(vendorProduct,
					productMap.get(vendorProduct.getProductID()));
			list.add(vpjp);
		}

		return list;
	}

}
