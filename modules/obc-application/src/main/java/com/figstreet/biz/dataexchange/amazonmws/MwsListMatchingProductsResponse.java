package com.figstreet.biz.dataexchange.amazonmws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ListMatchingProductsResponse;
import com.amazonservices.mws.products.model.ListMatchingProductsResult;
import com.amazonservices.mws.products.model.Product;

public class MwsListMatchingProductsResponse extends MwsMethodResponse
{
	private ListMatchingProductsResponse fResponse;

	private Map<String, MwsProduct> fMwsProductMap;

	public MwsListMatchingProductsResponse(ListMatchingProductsResponse pResponse, MwsIdType pIdType,
			String pIdentifier)
	{
		super(pResponse);
		this.fResponse = pResponse;
		this.fMwsProductMap = createMwsProductMap(this.fResponse, pIdType, pIdentifier);
	}

	public MwsListMatchingProductsResponse(MarketplaceWebServiceProductsException pException)
	{
		super(pException);
	}

	@Override
	public boolean isError()
	{
		return this.fResponse == null;
	}

	public Map<String, MwsProduct> getMwsProductMap()
	{
		return this.fMwsProductMap;
	}

	private static LinkedHashMap<String, MwsProduct> createMwsProductMap(ListMatchingProductsResponse pResponse,
			MwsIdType pIdType, String pIdentifier)
	{
		ArrayList<MwsProduct> mwsProductList;
		ListMatchingProductsResult result = pResponse.getListMatchingProductsResult();
		if (result.isSetProducts())
		{
			List<Product> resultProductList = result.getProducts().getProduct();
			mwsProductList = new ArrayList<>(resultProductList.size());

			for (Product resultProduct : resultProductList)
			{
				// Add all products to this list
				MwsProduct mwsProduct = new MwsProduct(pIdType, pIdentifier, resultProduct);
				mwsProductList.add(mwsProduct);
			}
		}
		else
		{
			mwsProductList = new ArrayList<>(1);
			MwsProduct mwsProduct = new MwsProduct(pIdType, pIdentifier, null);
			mwsProductList.add(mwsProduct);
		}

		if (mwsProductList.size() > 1)
		{
			MwsProductComparator comparator = new MwsProductComparator(
					MwsProductComparator.SortOrder.PRIMARY_SALES_RANK, false);
			Collections.sort(mwsProductList, comparator);
		}
		LinkedHashMap<String, MwsProduct> productMap = new LinkedHashMap<>(1);
		if (!mwsProductList.isEmpty())
			productMap.put(pIdentifier, mwsProductList.get(0));
		return productMap;
	}
}
