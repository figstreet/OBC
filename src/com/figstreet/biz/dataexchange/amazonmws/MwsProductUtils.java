package com.figstreet.biz.dataexchange.amazonmws;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.products.model.GetLowestPricedOffersForASINRequest;
import com.amazonservices.mws.products.model.GetMatchingProductForIdRequest;
import com.amazonservices.mws.products.model.ListMatchingProductsRequest;
import com.figstreet.biz.amazonpricing.AmazonPricingManager;
import com.figstreet.biz.amazonpricing.AmazonPricingWithLast;
import com.figstreet.biz.amazonsalesrank.AmazonSalesRankManager;
import com.figstreet.biz.amazonsalesrank.AmazonSalesRankWithLast;
import com.figstreet.biz.product.ProductManager;
import com.figstreet.biz.product.ProductWithLast;
import com.figstreet.biz.vendorproduct.VendorProductManager;
import com.figstreet.biz.vendorproduct.VendorProductWithLast;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.Logging;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.common.ProductCondition;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.product.ProductList;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorproduct.VendorProduct;

public class MwsProductUtils
{
	private static final String LOGGER_NAME = MwsProductUtils.class.getPackage().getName() + ".MwsProductUtils";

	/**
	 * Command line entry point.
	 *
	 * @throws SQLException
	 * @throws CloneNotSupportedException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws SQLException, CloneNotSupportedException, InterruptedException
	{
		try
		{
			SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");

			MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
			MwsController mwsController = new MwsController();
			UsersID by = UsersID.ADMIN;
			VendorID vendorID = VendorID.AMAZON_US;

			// Update existing Amazon VendorProduct, Product, AmazonPricing &
			// AmazonSalesRank records
			// that haven't been checked in X days

			// Create VendorProduct, AmazonPricing & AmazonSalesRank records for Amazon for
			// recently added Product records
			List<ProductID> amazonMissingIDList = ProductList.loadMissingFromVendor(vendorID, -1);

			// TODO - remove this limit
			int processLimit = 10;
			int lookupLimit = config.getMaxGetMatchingProductForId();
			LinkedHashSet<ProductID> lookupSet = new LinkedHashSet<>(lookupLimit);
			for (ProductID amazonMissingProductID : amazonMissingIDList)
			{
				lookupSet.add(amazonMissingProductID);
				if (lookupSet.size() >= lookupLimit)
				{
					try
					{
						processNewProductList(vendorID, lookupSet, mwsController, by);
					}
					catch (Exception e)
					{
						Logging.error(LOGGER_NAME, "main", "Error processing ProductIDs: " + lookupSet, e);
					}
					lookupSet.clear();

					processLimit--;
					if (processLimit <= 0)
						break;

					TimeUnit.MILLISECONDS.sleep(300);
				}
			}

			if (!lookupSet.isEmpty())
			{
				try
				{
					processNewProductList(vendorID, lookupSet, mwsController, by);
				}
				catch (Exception e)
				{
					Logging.error(LOGGER_NAME, "main", "Error processing ProductIDs: " + lookupSet, e);
				}
			}
		}
		finally
		{
			SystemInitializer.shutdown();
		}

	}

	public static void processNewProductList(VendorID pVendorID, Set<ProductID> pProductIDs,
			MwsController pMwsController, UsersID pBy)
			throws SQLException, MwsResponseException, CloneNotSupportedException
	{
		ProductList lookupList = ProductList.loadByProductIDs(pProductIDs);
		ArrayList<String> upcList = new ArrayList<>(lookupList.size());
		for (Product product : lookupList)
		{
			if (!CompareUtil.isEmpty(product.getUpc()))
				upcList.add(product.getUpc());
		}
		GetMatchingProductForIdRequest request = pMwsController.createGetMatchingProductsForIdRequest(MwsIdType.UPC,
				upcList);
		MwsMethodResponse response = pMwsController.getMatchingProductsForId(request);

		boolean responseOK = (response instanceof MwsGetProductForIdResponse) && !response.isError();
		if (!responseOK)
		{
			throw new MwsResponseException(response.fErrorMessage);
		}

		MwsGetProductForIdResponse mwsGetProductForIdResponse = (MwsGetProductForIdResponse) response;

		// TODO - remove this once development is complete
		System.out.println("RequestId: " + mwsGetProductForIdResponse.getHeaderRequestId());
		System.out.println("Timestamp: " + mwsGetProductForIdResponse.getHeaderTimestamp());
		System.out.println("Quota Max: " + mwsGetProductForIdResponse.getHeaderQuotaMax());
		System.out.println("Quota Remaining: " + mwsGetProductForIdResponse.getHeaderQuotaRemaining());
		System.out.println("Quota Resets: " + mwsGetProductForIdResponse.getHeaderQuotaResetsAt());

		Map<String, MwsProduct> mwsProductByUpcMap = mwsGetProductForIdResponse.getMwsProductMap();
		System.out.println("Products Downloaded: " + mwsProductByUpcMap.size());

		Map<String, MwsPricing> mwsPricingByAsinMap = new HashMap<>(mwsProductByUpcMap.size());
		for (Entry<String, MwsProduct> mwsProductByUpcEntry : mwsProductByUpcMap.entrySet())
		{
			MwsProduct mwsProduct = mwsProductByUpcEntry.getValue();
			String productUpc = mwsProductByUpcEntry.getKey();
			if (mwsProduct.isError() || CompareUtil.isEmpty(mwsProduct.getAsin()))
			{
				// TODO - log that GetMatchingProductForId didn't return a product for this UPC

				//Try the ListMatchingProducts API call
				ListMatchingProductsRequest lmpRequest = pMwsController.createListMatchingProductsRequest(productUpc);
				MwsMethodResponse lmpResponse = pMwsController.getListMatchingProducts(lmpRequest, MwsIdType.UPC, productUpc);

				if ((lmpResponse instanceof MwsListMatchingProductsResponse) && !lmpResponse.isError())
				{
					MwsListMatchingProductsResponse mwsListMatchingProductsResponse = (MwsListMatchingProductsResponse) lmpResponse;
					if (mwsListMatchingProductsResponse.isError())
					{
						//TODO - log that an error occurred
					}
					Map<String, MwsProduct> mwsProductMap = mwsListMatchingProductsResponse.getMwsProductMap();
					mwsProduct = mwsProductMap.get(productUpc);
					mwsProductByUpcMap.put(productUpc, mwsProduct);
				}
			}


			if (mwsProduct != null && !mwsProduct.isError())
			{
				GetLowestPricedOffersForASINRequest lowestPricedOfferRequest = pMwsController
						.createGetLowestPricedOffersForASINRequest(mwsProduct, ProductCondition.NEW);
				MwsMethodResponse lowestPricedOfferResponse = pMwsController
						.getLowestPricedOffersForASIN(lowestPricedOfferRequest);
				boolean lowestPricedOfferResponseOK = (lowestPricedOfferResponse instanceof MwsGetLowestPricedOffersResponse)
						&& !lowestPricedOfferResponse.isError();
				if (!lowestPricedOfferResponseOK)
				{
					throw new MwsResponseException(response.fErrorMessage);
				}

				MwsGetLowestPricedOffersResponse mwsGetLowestPricedOffersResponse = (MwsGetLowestPricedOffersResponse) lowestPricedOfferResponse;
				mwsPricingByAsinMap.put(mwsProduct.getAsin(), mwsGetLowestPricedOffersResponse.getMwsPricing());
			}
		}
		System.out.println("Pricing Downloaded: " + mwsPricingByAsinMap.size());

		for (Product product : lookupList)
		{
			try
			{
				MwsProduct mwsProduct = mwsProductByUpcMap.get(product.getUpc());
				MwsPricing mwsPricing = null;
				if (mwsProduct != null)
					mwsPricing = mwsPricingByAsinMap.get(mwsProduct.getAsin());
				syncNewAmazonData(pVendorID, product, mwsProduct, mwsPricing, pBy);
			}
			catch (Exception e)
			{
				//TODO - log an error, but we'll try the next product
				System.err.println("Error syncing ProductID " + product.getRecordID());
				e.printStackTrace(System.err);
			}
		}
	}

	public static void syncNewAmazonData(VendorID pVendorID, Product pProduct, MwsProduct pMwsProduct,
			MwsPricing pMwsPricing, UsersID pBy)
			throws IllegalArgumentException, CloneNotSupportedException, SQLException
	{
		syncAmazonData(pVendorID, pProduct, null, null, null, pMwsProduct, pMwsPricing, pBy);
	}

	public static void syncAmazonData(VendorID pVendorID, Product product, VendorProduct vendorProduct,
			AmazonPricing amazonPricing, AmazonSalesRank amazonSalesRank, MwsProduct mwsProduct, MwsPricing mwsPricing,
			UsersID pBy) throws CloneNotSupportedException, IllegalArgumentException, SQLException
	{
		if (mwsProduct == null || mwsProduct.isError())
		{
			// TODO - log this product wasn't found by UPC
			if (vendorProduct == null)
			{
				//If this is a new Amazon product, store a VendorProduct record
				vendorProduct = new VendorProduct(pVendorID, product.getRecordID(), pBy);
				vendorProduct.setActive(false);
				vendorProduct.setDownloaded(DateUtil.now());
				VendorProductManager.updateVendorProduct(vendorProduct, null, pBy);
			}
		}
		else
		{
			// sync fields in Product
			ProductWithLast productWithLast = new ProductWithLast(product);
			if (!syncProductWithMwsProduct(product, mwsProduct))
				productWithLast = null; // Not updating

			// create/update VendorProduct
			if (vendorProduct == null)
				vendorProduct = new VendorProduct(pVendorID, product.getRecordID(), pBy);
			VendorProductWithLast vpWithLast = new VendorProductWithLast(vendorProduct);
			if (!syncVendorProductWithMwsData(vendorProduct, mwsProduct, mwsPricing, pBy))
				vpWithLast = null; // Not updating

			// create/update AmazonPricing (VendorProductID can be null for now)
			if (amazonPricing == null)
				amazonPricing = new AmazonPricing(vendorProduct.getRecordID(), pBy);
			AmazonPricingWithLast apWithLast = new AmazonPricingWithLast(amazonPricing);
			if (!syncAmazonPricingWithMwsData(amazonPricing, mwsPricing, pBy))
				apWithLast = null; // not updating

			// create/update AmazonSalesRank (VendorProductID can be null for now)
			if (amazonSalesRank == null)
				amazonSalesRank = new AmazonSalesRank(vendorProduct.getRecordID(), pBy);
			AmazonSalesRankWithLast asrWithLast = new AmazonSalesRankWithLast(amazonSalesRank);
			if (!syncAmazonSalesRankWithMwsData(amazonSalesRank, mwsProduct, pBy))
				asrWithLast = null; // not updating

			HibernateTransaction trans = HibernateTransaction.open();
			try
			{
				if (productWithLast != null)
					ProductManager.updateProduct(productWithLast.getProduct(), productWithLast.getLastProduct(), pBy);
				if (vpWithLast != null)
					VendorProductManager.updateVendorProduct(vpWithLast.getVendorProduct(),
							vpWithLast.getLastVendorProduct(), pBy);

				if (apWithLast != null)
				{
					AmazonPricing azPricing = apWithLast.getAmazonPricing();
					azPricing.setVendorProductID(vendorProduct.getRecordID());
					AmazonPricingManager.updateAmazonPricing(azPricing, apWithLast.getLastAmazonPricing(), pBy);
				}

				if (asrWithLast != null)
				{
					AmazonSalesRank azSalesRank = asrWithLast.getAmazonSalesRank();
					azSalesRank.setVendorProductID(vendorProduct.getRecordID());
					AmazonSalesRankManager.updateAmazonSalesRank(azSalesRank, asrWithLast.getLastAmazonSalesRank(),
							pBy);
				}
				trans.commit();
			}
			finally
			{
				trans.close();
			}
		}
	}

	public static boolean syncProductWithMwsProduct(Product pProduct, MwsProduct pMwsProduct)
	{
		boolean dataChanged = false;
		if (!pProduct.isActive())
		{
			pProduct.setActive(true);
			dataChanged = true;
		}

		if (pMwsProduct != null)
		{
			// Will only override values in Product when they're null
			if (pProduct.getLength() == null && pMwsProduct.getLength() != null)
			{
				pProduct.setLength(pMwsProduct.getLength());
				pProduct.setLengthUnit(pMwsProduct.getLengthUnit());
				dataChanged = true;
			}

			if (pProduct.getWidth() == null && pMwsProduct.getWidth() != null)
			{
				pProduct.setWidth(pMwsProduct.getWidth());
				pProduct.setWidthUnit(pMwsProduct.getWidthUnit());
				dataChanged = true;
			}

			if (pProduct.getHeight() == null && pMwsProduct.getHeight() != null)
			{
				pProduct.setHeight(pMwsProduct.getHeight());
				pProduct.setHeightUnit(pMwsProduct.getHeightUnit());
				dataChanged = true;
			}

			if (pProduct.getWeight() == null && pMwsProduct.getWeight() != null)
			{
				pProduct.setWeight(pMwsProduct.getWeight());
				pProduct.setWeightUnit(pMwsProduct.getWeightUnit());
				dataChanged = true;
			}

			if (pProduct.getListPrice() == null && pMwsProduct.getListPrice() != null)
			{
				pProduct.setListPrice(pMwsProduct.getListPrice());
				pProduct.setPriceCurrency(pMwsProduct.getListPriceCurrency());
				dataChanged = true;
			}

			// Always update image URL
			if (!CompareUtil.isEmpty(pMwsProduct.getSmallImageURL())
					&& !CompareUtil.equals(pProduct.getImageUrl(), pMwsProduct.getSmallImageURL()))
			{
				pProduct.setImageUrl(pMwsProduct.getSmallImageURL());
				dataChanged = true;
			}
		}

		return dataChanged;
	}

	public static boolean syncVendorProductWithMwsData(VendorProduct pVendorProduct, MwsProduct pMwsProduct,
			MwsPricing pMwsPricing, UsersID pBy)
	{
		boolean dataChanged = false;
		if (!pVendorProduct.isActive())
		{
			pVendorProduct.setActive(true);
			dataChanged = true;
		}

		if (pMwsPricing != null)
		{
			Double buyboxPrice = pMwsPricing.getBuyboxPriceAsDouble();
			if (buyboxPrice != null && !CompareUtil.equals(buyboxPrice, pVendorProduct.getPrice()))
			{
				pVendorProduct.setPrice(buyboxPrice);
				pVendorProduct.setPriceCurrency(pMwsPricing.getPriceCurrency());
				dataChanged = true;
			}
			if (pVendorProduct.getQuantity() != pMwsPricing.getOfferCount())
			{
				pVendorProduct.setQuantity(pMwsPricing.getOfferCount());
				dataChanged = true;
			}
			if (pMwsPricing.getOfferCount() <= 0)
			{
				if (pVendorProduct.isAvailableOnline())
				{
					pVendorProduct.setAvailableOnline(false);
					dataChanged = true;
				}
			}
			else
			{
				if (!pVendorProduct.isAvailableOnline())
				{
					pVendorProduct.setAvailableOnline(true);
					dataChanged = true;
				}
			}
		}

		if (pMwsProduct != null)
		{
			if (!CompareUtil.isEmpty(pMwsProduct.getAsin())
					&& !CompareUtil.equalsString(pVendorProduct.getVendorIdentifier(), pMwsProduct.getAsin(), true))
			{
				if (!pVendorProduct.isNewRecord())
				{
					// TODO - log that the VendorIdentifier/ASIN changed
				}
				pVendorProduct.setVendorIdentifier(pMwsProduct.getAsin());
				dataChanged = true;
			}

			if (pMwsProduct.getMarketplace() != null
					&& !CompareUtil.equals(pVendorProduct.getAmazonMarketplace(), pMwsProduct.getMarketplace()))
			{
				pVendorProduct.setAmazonMarketplace(pMwsProduct.getMarketplace());
				dataChanged = true;
			}
		}

		if (dataChanged)
			pVendorProduct.setDownloaded(DateUtil.now());

		return dataChanged;
	}

	public static boolean syncAmazonPricingWithMwsData(AmazonPricing pAmazonPricing, MwsPricing pMwsPricing,
			UsersID pBy)
	{
		boolean dataChanged = false;

		if (pMwsPricing != null)
		{
			if (!CompareUtil.equals(pAmazonPricing.getProductCondition(), pMwsPricing.getProductCondition()))
			{
				pAmazonPricing.clearPricingFields(); // reset all fields if condition changes
				pAmazonPricing.setProductCondition(pMwsPricing.getProductCondition());
				dataChanged = true;
			}

			if (!CompareUtil.equals(pAmazonPricing.getOfferCount(), pMwsPricing.getOfferCount()))
			{
				pAmazonPricing.setOfferCount(pMwsPricing.getOfferCount());
				dataChanged = true;
			}

			if (!CompareUtil.equals(pAmazonPricing.getPriceCurrency(), pMwsPricing.getPriceCurrency()))
			{
				pAmazonPricing.setPriceCurrency(pMwsPricing.getPriceCurrency());
				dataChanged = true;
			}

			Double buyboxItemPrice = pMwsPricing.getBuyboxItemPriceAsDouble();
			if (!CompareUtil.equals(pAmazonPricing.getBuyboxItemPrice(), buyboxItemPrice))
			{
				pAmazonPricing.setBuyboxItemPrice(buyboxItemPrice);
				dataChanged = true;
			}

			Double buyboxShippingPrice = pMwsPricing.getBuyboxShippingPriceAsDouble();
			if (!CompareUtil.equals(pAmazonPricing.getBuyboxShippingPrice(), buyboxShippingPrice))
			{
				pAmazonPricing.setBuyboxShippingPrice(buyboxShippingPrice);
				dataChanged = true;
			}

			if (pAmazonPricing.isBuyboxFBA() != pMwsPricing.isBuyboxFBA())
			{
				pAmazonPricing.setBuyboxFBA(pMwsPricing.isBuyboxFBA());
				dataChanged = true;
			}

			Double secondaryItemPrice = pMwsPricing.getSecondaryItemPriceAsDouble();
			if (!CompareUtil.equals(pAmazonPricing.getSecondaryItemPrice(), secondaryItemPrice))
			{
				pAmazonPricing.setSecondaryItemPrice(secondaryItemPrice);
				dataChanged = true;
			}

			Double secondaryShippingPrice = pMwsPricing.getSecondaryShippingPriceAsDouble();
			if (!CompareUtil.equals(pAmazonPricing.getSecondaryShippingPrice(), secondaryShippingPrice))
			{
				pAmazonPricing.setSecondaryShippingPrice(secondaryShippingPrice);
				dataChanged = true;
			}

			if (pAmazonPricing.isSecondaryFBA() != pMwsPricing.isSecondaryFBA())
			{
				pAmazonPricing.setSecondaryFBA(pMwsPricing.isSecondaryFBA());
				dataChanged = true;
			}
		}

		if (dataChanged)
			pAmazonPricing.setDownloaded(DateUtil.now());

		return dataChanged;
	}

	public static boolean syncAmazonSalesRankWithMwsData(AmazonSalesRank pAmazonSalesRank, MwsProduct pMwsProduct,
			UsersID pBy)
	{
		boolean dataChanged = false;

		if (pMwsProduct != null)
		{
			Integer mwsPrimarySR = pMwsProduct.getPrimarySalesRank();
			if (!CompareUtil.equals(pAmazonSalesRank.getPrimaryRank(), mwsPrimarySR))
			{
				pAmazonSalesRank.setPrimaryRank(mwsPrimarySR);
				dataChanged = true;
			}
			AmazonSalesRankCategory mwsPrimarySRC = pMwsProduct.getPrimarySalesRankCategory();
			if (!CompareUtil.equals(pAmazonSalesRank.getPrimarySalesRankCategory(), mwsPrimarySRC))
			{
				pAmazonSalesRank.setPrimarySalesRankCategory(mwsPrimarySRC);
				dataChanged = true;
			}

			Integer mwsSecondarySR = pMwsProduct.getSecondarySalesRank();
			if (!CompareUtil.equals(pAmazonSalesRank.getSecondaryRank(), mwsSecondarySR))
			{
				pAmazonSalesRank.setSecondaryRank(mwsSecondarySR);
				dataChanged = true;
			}
			AmazonSalesRankCategory mwsSecondarySRC = pMwsProduct.getSecondarySalesRankCategory();
			if (!CompareUtil.equals(pAmazonSalesRank.getSecondarySalesRankCategory(), mwsSecondarySRC))
			{
				pAmazonSalesRank.setSecondarySalesRankCategory(mwsSecondarySRC);
				dataChanged = true;
			}
		}

		if (dataChanged)
			pAmazonSalesRank.setDownloaded(DateUtil.now());

		return dataChanged;
	}
}
