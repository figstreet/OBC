package com.figstreet.biz.dataexchange.amazonmws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.GetMatchingProductForIdResponse;
import com.amazonservices.mws.products.model.GetMatchingProductForIdResult;
import com.amazonservices.mws.products.model.Product;

public class MwsGetProductForIdResponse extends MwsMethodResponse
{
	// The GetMatchingProductForIdResponse can contain many responses
	// each response can contain many products

	private GetMatchingProductForIdResponse fResponse;

	private Map<String, MwsProduct> fMwsProductMap;

	public MwsGetProductForIdResponse(GetMatchingProductForIdResponse pResponse)
	{
		super(pResponse);
		this.fResponse = pResponse;
		this.fMwsProductMap = createMwsProductMap(this.fResponse);
	}

	public MwsGetProductForIdResponse(MarketplaceWebServiceProductsException pException)
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

	private static LinkedHashMap<String, MwsProduct> createMwsProductMap(GetMatchingProductForIdResponse pResponse)
	{
		// Create a LinkedHashMap <Identifier, ArrayList<MwsProduct>>
		// Sort each ArrayList<MwsProduct> by primary sales rank
		// Return a LinkedHashMap <Identifier, MwsProduct> from list at position 0

		List<GetMatchingProductForIdResult> matchingProductForResultList = pResponse.getGetMatchingProductForIdResult();
		LinkedHashMap<String, ArrayList<MwsProduct>> mapByIdentifier = new LinkedHashMap<>(
				matchingProductForResultList.size());
		for (GetMatchingProductForIdResult result : matchingProductForResultList)
		{
			String identifier = result.getId();
			if (result.isSetError())
			{
				ArrayList<MwsProduct> mwsProductList = mapByIdentifier.get(identifier);
				if (mwsProductList == null)
				{
					// Create the list if it doesn't already exist
					mwsProductList = new ArrayList<>(1);
					mapByIdentifier.put(identifier, mwsProductList);
				}
				MwsProduct mwsProduct = new MwsProduct(result);
				mwsProductList.add(mwsProduct);
			}
			else
			{
				List<Product> resultProductList = result.getProducts().getProduct();
				ArrayList<MwsProduct> mwsProductList = mapByIdentifier.get(identifier);
				if (mwsProductList == null)
				{
					// Create the list if it doesn't already exist
					mwsProductList = new ArrayList<>(resultProductList.size());
					mapByIdentifier.put(identifier, mwsProductList);
				}

				for (Product resultProduct : resultProductList)
				{
					// Add all products to this list
					MwsProduct mwsProduct = new MwsProduct(result, resultProduct);
					mwsProductList.add(mwsProduct);
				}
			}
		}

		MwsProductComparator comparator = new MwsProductComparator(MwsProductComparator.SortOrder.PRIMARY_SALES_RANK, false);

		LinkedHashMap<String, MwsProduct> productMap = new LinkedHashMap<>(mapByIdentifier.size());
		for (Map.Entry<String, ArrayList<MwsProduct>> byIdentifierEntry : mapByIdentifier.entrySet())
		{
			ArrayList<MwsProduct> byIdentifierList = byIdentifierEntry.getValue();
			if (byIdentifierList.size() > 1)
				Collections.sort(byIdentifierList, comparator);

			productMap.put(byIdentifierEntry.getKey(), byIdentifierList.get(0));
		}
		return productMap;
	}
}
