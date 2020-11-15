package com.figstreet.biz.dataexchange.amazonmws;

import java.math.BigDecimal;

import com.amazonservices.mws.products.model.ASINOfferDetail;
import com.amazonservices.mws.products.model.MoneyType;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.common.ProductCondition;

public class MwsOffer
{
	private ProductCondition fProductCondition;
	private PriceCurrency fPriceCurrency;
	private BigDecimal fProductCost;
	private BigDecimal fShippingCost;
	private boolean fIsFba;
	private boolean fIsBuyBoxWinner;
	private boolean fIsFeaturedMerchant;

	public MwsOffer(ASINOfferDetail pOfferDetail)
	{
		this.fProductCondition = ProductCondition.newInstance(pOfferDetail.getSubCondition());
		MoneyType listingPrice = pOfferDetail.getListingPrice();
		if (listingPrice != null)
		{
			this.fPriceCurrency = PriceCurrency.newInstance(listingPrice.getCurrencyCode());
			this.fProductCost = listingPrice.getAmount();
		}
		MoneyType shippingCost = pOfferDetail.getShipping();
		if (shippingCost != null)
		{
			if (this.fPriceCurrency == null)
				this.fPriceCurrency = PriceCurrency.newInstance(shippingCost.getCurrencyCode());
			this.fShippingCost = shippingCost.getAmount();
		}

		this.fIsBuyBoxWinner = pOfferDetail.getIsBuyBoxWinner() != null ? pOfferDetail.getIsBuyBoxWinner() : false;
		this.fIsFba = pOfferDetail.getIsFulfilledByAmazon();
		this.fIsFeaturedMerchant = pOfferDetail.getIsFeaturedMerchant() != null ? pOfferDetail.getIsFeaturedMerchant()
				: false;
	}

	public ProductCondition getProductCondition()
	{
		return this.fProductCondition;
	}

	public PriceCurrency getPriceCurrency()
	{
		return this.fPriceCurrency;
	}

	public BigDecimal getProductCost()
	{
		return this.fProductCost;
	}

	public BigDecimal getShippingCost()
	{
		return this.fShippingCost;
	}

	public Double getProductCostAsDouble()
	{
		if (this.fProductCost == null)
			return null;
		return this.fProductCost.doubleValue();
	}

	public Double getShippingCostAsDouble()
	{
		if (this.fShippingCost == null)
			return null;
		return this.fShippingCost.doubleValue();
	}

	public boolean getIsFba()
	{
		return this.fIsFba;
	}

	public boolean getIsBuyBoxWinner()
	{
		return this.fIsBuyBoxWinner;
	}

	public boolean getIsFeaturedMerchant()
	{
		return this.fIsFeaturedMerchant;
	}

}
