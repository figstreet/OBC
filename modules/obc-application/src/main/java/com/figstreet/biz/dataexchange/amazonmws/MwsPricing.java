package com.figstreet.biz.dataexchange.amazonmws;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amazonservices.mws.products.model.ASINOfferDetail;
import com.amazonservices.mws.products.model.ASINOfferDetailList;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForASINResult;
import com.figstreet.core.CompareUtil;
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.common.ProductCondition;

public class MwsPricing
{
	private GetLowestPricedOffersForASINResult fLowestProcedOffersResult;

	private String fAsin; // VendorIdentifier
	private AmazonMarketplace fMarketplace;

	private ProductCondition fProductCondition;
	private PriceCurrency fPriceCurrency;
	private BigDecimal fBuyboxItemPrice;
	private BigDecimal fBuyboxShippingPrice;
	private boolean fBuyboxFBA;

	private BigDecimal fSecondaryItemPrice;
	private BigDecimal fSecondaryShippingPrice;
	private boolean fSecondaryFBA;

	private List<MwsOffer> fMwsOfferList;

	public MwsPricing(GetLowestPricedOffersForASINResult pLowestProcedOffersResult)
	{
		this.fLowestProcedOffersResult = pLowestProcedOffersResult;
		this.importResult();
	}

	private void importResult()
	{
		this.fAsin = this.fLowestProcedOffersResult.getASIN();
		this.fMarketplace = AmazonMarketplace.newInstance(this.fLowestProcedOffersResult.getMarketplaceID());
		this.fProductCondition = ProductCondition.newInstance(this.fLowestProcedOffersResult.getItemCondition());

		ASINOfferDetailList offerDetails = this.fLowestProcedOffersResult.getOffers();
		if (offerDetails != null)
		{
			List<ASINOfferDetail> offerDetailList = offerDetails.getOffer();
			this.fMwsOfferList = new ArrayList<>(offerDetailList.size());
			boolean buyboxFound = false;

			for (ASINOfferDetail offerDetail : offerDetailList)
			{
				MwsOffer mwsOffer = new MwsOffer(offerDetail);
				if (CompareUtil.equals(mwsOffer.getProductCondition(), this.fProductCondition))
				{
					this.fMwsOfferList.add(mwsOffer);
					if (this.fPriceCurrency == null)
						this.fPriceCurrency = mwsOffer.getPriceCurrency();

					if (mwsOffer.getIsBuyBoxWinner())
					{
						buyboxFound = true;
						this.fBuyboxFBA = mwsOffer.getIsFba();
						this.fBuyboxItemPrice = mwsOffer.getProductCost();
						this.fBuyboxShippingPrice = mwsOffer.getShippingCost();
					}
				}
				else
				{
					// TODO Log an error that the product condition didn't match
				}
			}

			//If Amazon doesn't have a buybox offer, use the first offer
			if (!buyboxFound && !this.fMwsOfferList.isEmpty())
			{
				MwsOffer buyboxOffer = this.fMwsOfferList.get(0);
				this.fBuyboxItemPrice = buyboxOffer.getProductCost();
				this.fBuyboxShippingPrice = buyboxOffer.getShippingCost();
				this.fBuyboxFBA = buyboxOffer.getIsFba();
			}

			//Identify the secondary offer as the 11th offer (second page) or the middle offer if less than 10 offers
			if (this.fMwsOfferList.size() > 1)
			{
				int offerIndex = 10;
				if (offerIndex >= this.fMwsOfferList.size())
				{
					offerIndex = this.fMwsOfferList.size() / 2;
					if (offerIndex == 0)
						offerIndex = 1;
				}

				MwsOffer secondaryOffer = this.fMwsOfferList.get(offerIndex);
				this.fSecondaryItemPrice = secondaryOffer.getProductCost();
				this.fSecondaryShippingPrice = secondaryOffer.getShippingCost();
				this.fSecondaryFBA = secondaryOffer.getIsFba();
			}

		}
	}

	public String getAsin()
	{
		return this.fAsin;
	}

	public AmazonMarketplace getMarketplace()
	{
		return this.fMarketplace;
	}

	public ProductCondition getProductCondition()
	{
		return this.fProductCondition;
	}

	public int getOfferCount()
	{
		if (this.fMwsOfferList == null)
			return 0;
		return this.fMwsOfferList.size();
	}

	public PriceCurrency getPriceCurrency()
	{
		return this.fPriceCurrency;
	}

	public BigDecimal getBuyboxItemPrice()
	{
		return this.fBuyboxItemPrice;
	}

	public BigDecimal getBuyboxShippingPrice()
	{
		return this.fBuyboxShippingPrice;
	}

	public Double getBuyboxPriceAsDouble()
	{
		BigDecimal buyboxPrice;
		if (this.fBuyboxItemPrice != null)
		{
			if (this.fBuyboxShippingPrice != null)
				buyboxPrice = this.fBuyboxItemPrice.add(this.fBuyboxShippingPrice);
			else
				buyboxPrice = this.fBuyboxItemPrice;
		}
		else
			buyboxPrice = this.fBuyboxShippingPrice;

		if (buyboxPrice != null)
			return buyboxPrice.doubleValue();

		return null;
	}

	public Double getBuyboxItemPriceAsDouble()
	{
		if (this.fBuyboxItemPrice == null)
			return null;
		return this.fBuyboxItemPrice.doubleValue();
	}

	public Double getBuyboxShippingPriceAsDouble()
	{
		if (this.fBuyboxShippingPrice == null)
			return null;
		return this.fBuyboxShippingPrice.doubleValue();
	}

	public boolean isBuyboxFBA()
	{
		return this.fBuyboxFBA;
	}

	public List<MwsOffer> getMwsOfferList()
	{
		return this.fMwsOfferList;
	}

	public BigDecimal getSecondaryItemPrice()
	{
		return this.fSecondaryItemPrice;
	}

	public BigDecimal getSecondaryShippingPrice()
	{
		return this.fSecondaryShippingPrice;
	}

	public boolean isSecondaryFBA()
	{
		return this.fSecondaryFBA;
	}

	public Double getSecondaryPriceAsDouble()
	{
		BigDecimal secondaryPrice;
		if (this.fSecondaryItemPrice != null)
		{
			if (this.fSecondaryShippingPrice != null)
				secondaryPrice = this.fSecondaryItemPrice.add(this.fSecondaryShippingPrice);
			else
				secondaryPrice = this.fSecondaryItemPrice;
		}
		else
			secondaryPrice = this.fSecondaryShippingPrice;

		if (secondaryPrice != null)
			return secondaryPrice.doubleValue();

		return null;
	}

	public Double getSecondaryItemPriceAsDouble()
	{
		if (this.fSecondaryItemPrice == null)
			return null;
		return this.fSecondaryItemPrice.doubleValue();
	}

	public Double getSecondaryShippingPriceAsDouble()
	{
		if (this.fSecondaryShippingPrice == null)
			return null;
		return this.fSecondaryShippingPrice.doubleValue();
	}

}
